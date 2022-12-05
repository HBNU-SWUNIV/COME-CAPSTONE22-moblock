package com.example.hyperledgervirtualmoneyproject.ui.home;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hyperledgervirtualmoneyproject.API.UserApi;
import com.example.hyperledgervirtualmoneyproject.API.UserTradeApi;
import com.example.hyperledgervirtualmoneyproject.DTO.ErrorBody;
import com.example.hyperledgervirtualmoneyproject.DTO.JwtToken;
import com.example.hyperledgervirtualmoneyproject.DTO.UserGetAssetDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserTradeHistoryResponseDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserTradeResponseDTO;
import com.example.hyperledgervirtualmoneyproject.LoadingDialog;
import com.example.hyperledgervirtualmoneyproject.R;
import com.example.hyperledgervirtualmoneyproject.ui.transfer.TradeRecycler.Adapter;
import com.example.hyperledgervirtualmoneyproject.ui.transfer.TradeRecycler.PaintTitle;
import com.example.hyperledgervirtualmoneyproject.databinding.FragmentHomeBinding;
import com.example.hyperledgervirtualmoneyproject.ui.home.CoinListView.CoinData;
import com.example.hyperledgervirtualmoneyproject.ui.home.CoinListView.CoinLIstAdapter;
import com.example.hyperledgervirtualmoneyproject.ui.transfer.userTradeActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final String TAG = "HomeFragment";

    TextView coinName, amount;
    Button btnTradeListShow;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    private int page = 1;
    ArrayList<PaintTitle> myDataset = new ArrayList<>();
    LoadingDialog customProgressDialog;
    ConstraintLayout tradeLayout;
    ImageView coinImg;

    ListView coinListView;
    ArrayList<CoinData> movieDataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        coinName = (TextView) root.findViewById(R.id.home_coinName);
        amount = (TextView) root.findViewById(R.id.home_amount);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.home_tradeList);
        coinListView = (ListView) root.findViewById(R.id.coinListView);
        tradeLayout = (ConstraintLayout) root.findViewById(R.id.trade_list_layout);


        coinImg = (ImageView) root.findViewById(R.id.coinGif);
        Glide.with(this).load(R.drawable.coin).into(coinImg);

        getAssetService();
        populateData();
        initAdapter();

        customProgressDialog = new LoadingDialog(getContext());
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (customProgressDialog.isShowing()) {
                            customProgressDialog.cancel();
                        }
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 10000);
            }
        });
        thread.start();

        tradeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), userTradeActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getAssetService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);

        System.out.println("jwtToken = " + JwtToken.getJwt());
        Call<UserGetAssetDTO> call = service.getAsset(JwtToken.getJwt());

        Toast toast = Toast.makeText(getContext(), "자산 정보를 불러오는 중...", Toast.LENGTH_LONG);
        toast.show();

        call.enqueue(new Callback<UserGetAssetDTO>() {
            @Override
            public void onResponse(Call<UserGetAssetDTO> call, Response<UserGetAssetDTO> response) {
                if(response.isSuccessful()){
                    UserGetAssetDTO result = response.body();

                    movieDataList = new ArrayList<CoinData>();

                    for (String key : result.getCoin().keySet()) {
                        movieDataList.add(new CoinData(key,result.getCoin().get(key)));
                    }

                    final CoinLIstAdapter myAdapter = new CoinLIstAdapter(getContext(), movieDataList);

                    coinListView.setAdapter(myAdapter);

                    toast.cancel();
                    Toast.makeText(getContext(), "완료", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: 성공, 결과 \n" + result.toString());
                }else{
                    Toast.makeText(getContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: 실패");
                    try {
                        String errorMessage = response.errorBody().string();
                        ObjectMapper objectMapper = new ObjectMapper();
                        ErrorBody errorBody = objectMapper.readValue(errorMessage, ErrorBody.class);
                        if (errorBody.getMessage().equals("잘못된 식별 번호로 요청했습니다")) {
                            Toast.makeText(getContext(), "다시 로그인 해주세요", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserGetAssetDTO> call, Throwable t) {
                Toast.makeText(getContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"onFailure" + t.getMessage());
            }
        });
    }

    private void populateData() {
        getUserTradeHistory(page);
    }

    private void initAdapter() {
        mAdapter = new Adapter(myDataset);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void getUserTradeHistory(int pageInit){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserTradeApi service = retrofit.create(UserTradeApi.class);

        System.out.println("jwtToken = " + JwtToken.getJwt());
        Call<UserTradeHistoryResponseDTO> call = service.trade(JwtToken.getJwt(), pageInit);
        Toast loadingToast = Toast.makeText(getContext(), "기록을 불러오는 중...", Toast.LENGTH_SHORT);
        loadingToast.show();

        call.enqueue(new Callback<UserTradeHistoryResponseDTO>() {
            @Override
            public void onResponse(Call<UserTradeHistoryResponseDTO> call, Response<UserTradeHistoryResponseDTO> response) {
                if(response.isSuccessful()){
                    System.out.println(page + "------");
                    UserTradeHistoryResponseDTO result = response.body();
                    List<UserTradeResponseDTO> tradeResponseList = result.getTransferResponseList();
                    for (UserTradeResponseDTO userTradeResponseDTO : tradeResponseList) {

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        LocalDateTime dateTime = LocalDateTime.parse(userTradeResponseDTO.getDateCreated(), formatter);
                        String yyMd = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        System.out.println("JwtToken.getId() = " + JwtToken.getId());
                        System.out.println("userTradeResponseDTO = " + userTradeResponseDTO.getSenderIdentifier().toString());
                        if(JwtToken.getId().equals(userTradeResponseDTO.getSenderIdentifier().toString())){
                            System.out.println("dateTime = " + dateTime);
                            myDataset.add(new PaintTitle
                                    (
                                            userTradeResponseDTO.getReceiverIdentifier(), userTradeResponseDTO.getReceiverName(),
                                            userTradeResponseDTO.getCoinName(), "-" + userTradeResponseDTO.getAmount().toString(),
                                            yyMd, dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                                    )
                            );
                            System.out.println(dateTime);
                        }else{
                            myDataset.add(new PaintTitle
                                    (
                                            userTradeResponseDTO.getSenderIdentifier(), userTradeResponseDTO.getSenderName(),
                                            userTradeResponseDTO.getCoinName(), userTradeResponseDTO.getAmount().toString(),
                                            yyMd, dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                                    )
                            );

                        }

                    }
                    mAdapter.notifyDataSetChanged();
                    System.out.println("page: " + page);
                    loadingToast.cancel();
                    customProgressDialog.cancel();
                    if(result.toString() == "[]"){
                        Toast.makeText(getContext(), "더 이상 기록이 없습니다.", Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(getContext(), "완료", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: 성공, 결과 \n" + result.toString());
                }else{
                    Log.d(TAG, "onResponse: 실패");
                    try {
                        String errorMessage = response.errorBody().string();
                        ObjectMapper objectMapper = new ObjectMapper();
                        ErrorBody errorBody = objectMapper.readValue(errorMessage, ErrorBody.class);
                        if (errorBody.getMessage().equals("잘못된 식별 번호로 요청했습니다")) {
                            Toast.makeText(getContext(), "다시 로그인 해주세요", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserTradeHistoryResponseDTO> call, Throwable t) {
                Log.d(TAG,"onFailure" + t.getMessage());
            }
        });
    }
}