package com.example.hyperledgervirtualmoneyproject.ui.transfer;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyperledgervirtualmoneyproject.API.UserTradeApi;
import com.example.hyperledgervirtualmoneyproject.DTO.ErrorBody;
import com.example.hyperledgervirtualmoneyproject.DTO.JwtToken;
import com.example.hyperledgervirtualmoneyproject.DTO.UserTradeHistoryResponseDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserTradeResponseDTO;
import com.example.hyperledgervirtualmoneyproject.LoadingDialog;
import com.example.hyperledgervirtualmoneyproject.MainActivity;
import com.example.hyperledgervirtualmoneyproject.R;
import com.example.hyperledgervirtualmoneyproject.ui.transfer.TradeRecycler.Adapter;
import com.example.hyperledgervirtualmoneyproject.ui.transfer.TradeRecycler.PaintTitle;
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

public class userTradeActivity extends AppCompatActivity {

    LoadingDialog customProgressDialog;

    private static final String TAG = "userTradeActivity";
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    private int page = 1;
    boolean isLoading = false;

    ArrayList<PaintTitle> myDataset = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertradehistory);

        mRecyclerView = (RecyclerView) findViewById(R.id.TradeRecycler);
        populateData();
        initAdapter();
        initScrollListener();


        customProgressDialog = new LoadingDialog(userTradeActivity.this);
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
    }

    private void populateData() {
        getUserTradeHistory(page);
    }

    private void initAdapter() {
        mAdapter = new Adapter(myDataset);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initScrollListener(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(!isLoading) {
                    if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == myDataset.size() - 1){
                        loadMore();
                        isLoading = true;
                    }

                }
            }
        });
    }

    private void loadMore(){
        myDataset.add(null);
        mAdapter.notifyItemInserted(myDataset.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserTradeHistory(page);
                isLoading = false;
            }
        }, 2000);
    }

    public void getUserTradeHistory(int pageInit) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserTradeApi service = retrofit.create(UserTradeApi.class);

        System.out.println("jwtToken = " + JwtToken.getJwt());
        Log.d(TAG, "pageInit: " + pageInit);
        Call<UserTradeHistoryResponseDTO> call = service.trade(JwtToken.getJwt(), pageInit);
        Toast loadingToast = Toast.makeText(getApplicationContext(), "기록을 불러오는 중...", Toast.LENGTH_SHORT);
        loadingToast.show();

        call.enqueue(new Callback<UserTradeHistoryResponseDTO>() {
            @Override
            public void onResponse(Call<UserTradeHistoryResponseDTO> call, Response<UserTradeHistoryResponseDTO> response) {
                System.out.println("response = " + response.toString());
                if(response.isSuccessful()){
                    System.out.println(page + "----------");
                    if(page > 1){
                        myDataset.remove(myDataset.size() - 1);
                        mAdapter.notifyItemRemoved(myDataset.size());
                    }
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
                                            userTradeResponseDTO.getReceiverIdentifier().toString(), userTradeResponseDTO.getReceiverName(),
                                            userTradeResponseDTO.getCoinName(), "-" + userTradeResponseDTO.getAmount().toString(),
                                            yyMd, dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                                    )
                            );
                            System.out.println(dateTime);
                        }else{
                            myDataset.add(new PaintTitle
                                    (
                                            userTradeResponseDTO.getSenderIdentifier().toString(), userTradeResponseDTO.getSenderName(),
                                            userTradeResponseDTO.getCoinName(), userTradeResponseDTO.getAmount().toString(),
                                            yyMd, dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                                    )
                            );

                        }

                    }
                    mAdapter.notifyDataSetChanged();
                    page++;
                    loadingToast.cancel();
                    if(result.toString() == "[]"){
                        Toast.makeText(getApplicationContext(), "더 이상 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: 성공, 결과 \n" + result.toString());
                    customProgressDialog.cancel();
                }else{
                    Log.d(TAG, "onResponse: 실패");
                    try {
                        String errorMessage = response.errorBody().string();
                        ObjectMapper objectMapper = new ObjectMapper();
                        ErrorBody errorBody = objectMapper.readValue(errorMessage, ErrorBody.class);
                        if (errorBody.getMessage().equals("잘못된 식별 번호로 요청했습니다")) {
                            Toast.makeText(getApplicationContext(), "다시 로그인 해주세요", Toast.LENGTH_SHORT).show();
                            ActivityCompat.finishAffinity(userTradeActivity.this);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserTradeHistoryResponseDTO> call, Throwable t) {
                Log.d(TAG,"onFailure " + t.getMessage());
            }
        });
    }
}
