package com.example.hyperledgervirtualmoneyproject.ui.shopList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyperledgervirtualmoneyproject.API.ShopListApi;
import com.example.hyperledgervirtualmoneyproject.DTO.ErrorBody;
import com.example.hyperledgervirtualmoneyproject.DTO.JwtToken;
import com.example.hyperledgervirtualmoneyproject.DTO.UserShopListDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserShopListResponseDTO;
import com.example.hyperledgervirtualmoneyproject.LoadingDialog;
import com.example.hyperledgervirtualmoneyproject.R;
import com.example.hyperledgervirtualmoneyproject.ui.shopList.ShopListRecycler.ShopListAdapter;
import com.example.hyperledgervirtualmoneyproject.ui.shopList.ShopListRecycler.ShopListPaintTitle;
import com.example.hyperledgervirtualmoneyproject.databinding.FragmentShoplistBinding;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopListFragment extends Fragment {

    private static final String TAG = "ShopListFragment";

    private FragmentShoplistBinding binding;

    RecyclerView mRecyclerView;
    ShopListAdapter mAdapter;

    private int page = 1;
    boolean isLoading = false;

    LoadingDialog customProgressDialog;

    ArrayList<ShopListPaintTitle> myDataset = new ArrayList<>();

    Button btnSearch;
    EditText etSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ShopListViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ShopListViewModel.class);

        binding = FragmentShoplistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mRecyclerView = (RecyclerView) root.findViewById(R.id.shopList_recycler);
        btnSearch = (Button) root.findViewById(R.id.shopListSearchBtn);
        etSearch = (EditText) root.findViewById(R.id.shopListSearchText);

        getShopList(page);
        initAdapter();
        initScrollListener();

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mAdapter.setOnItemClickListener(new ShopListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, String data) {
                Web(data);
            }
        });

        return root;
    }

    public void Web(String URL){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(URL));
        startActivity(browserIntent);
    }

    private void initAdapter() {
        Log.d(TAG, "initAdapter");
        mAdapter = new ShopListAdapter(myDataset);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initScrollListener(){
        Log.d(TAG, "initScrollListener");
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getShopList(page);
                isLoading = false;
            }
        }, 2000);
    }

    public void getShopList(int pageInit){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopListApi service = retrofit.create(ShopListApi.class);

        System.out.println("jwtToken = " + JwtToken.getJwt());
        Call<UserShopListResponseDTO> call = service.getStoreList(JwtToken.getJwt(), pageInit);

        Toast loadingToast = Toast.makeText(getContext(), "가맹점 리스트를 불러오는 중...", Toast.LENGTH_SHORT);
        loadingToast.show();

        call.enqueue(new Callback<UserShopListResponseDTO>() {
            @Override
            public void onResponse(Call<UserShopListResponseDTO> call, Response<UserShopListResponseDTO> response) {
                if(response.isSuccessful()){
                    System.out.println(page + "------");

                    UserShopListResponseDTO result = response.body();
                    List<UserShopListDTO> storeResponseList = result.getStoreResponseList();
                    System.out.println("storeResponseList = " + storeResponseList);


                    if(result.toString() == "[]"){
                        System.out.println("끝");
                    }else{
                        System.out.println("실행됨");
                        for (UserShopListDTO userShopListDTO : storeResponseList) {

                            Call<ResponseBody> call2 = service.getStoreImage(JwtToken.getJwt(), userShopListDTO.getStoreImageFileName());
                            Bitmap bitmap = null;
                            try {
                                bitmap = new getImg().execute(call2).get();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (bitmap == null) {
                                myDataset.add(new ShopListPaintTitle
                                        (
                                                BitmapFactory.decodeResource(getResources(), R.drawable.scan_qr), userShopListDTO.getName(),
                                                userShopListDTO.getPhoneNumber(), userShopListDTO.getAddress()
                                        )
                                );
                            } else {
                                myDataset.add(new ShopListPaintTitle
                                        (
                                                bitmap, userShopListDTO.getName(), userShopListDTO.getPhoneNumber(), userShopListDTO.getAddress()
                                        )
                                );
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    page++;
                    loadingToast.cancel();
                    Log.d(TAG, "onResponse: 성공\n결과:" + result);
                    customProgressDialog.cancel();
                }else{
                    Log.d(TAG, "onResponse: 실패\n에러메시지:" + response);
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
            public void onFailure(Call<UserShopListResponseDTO> call, Throwable t) {
                Log.d(TAG,"onFailure" + t.getMessage());
            }
        });
    }

    private class getImg extends AsyncTask<Call, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Call... calls) {
            try {
                Call<ResponseBody> call = calls[0];
                Response<ResponseBody> execute = call.execute();
                Bitmap bitmap = BitmapFactory.decodeStream(execute.body().byteStream());

                return bitmap;
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}