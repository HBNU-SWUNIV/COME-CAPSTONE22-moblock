package com.example.hyperledgervirtualmoneyproject.ui.user;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hyperledgervirtualmoneyproject.API.UserApi;
import com.example.hyperledgervirtualmoneyproject.DTO.ErrorBody;
import com.example.hyperledgervirtualmoneyproject.DTO.JwtToken;
import com.example.hyperledgervirtualmoneyproject.DTO.UserGetAssetDTO;
import com.example.hyperledgervirtualmoneyproject.LoadingDialog;
import com.example.hyperledgervirtualmoneyproject.MainActivity;
import com.example.hyperledgervirtualmoneyproject.MainLoginActivity;
import com.example.hyperledgervirtualmoneyproject.R;
import com.example.hyperledgervirtualmoneyproject.databinding.FragmentUserBinding;
import com.example.hyperledgervirtualmoneyproject.ui.transfer.userTradeActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private Button logout, passwordChange;
    private TextView studentId, studentName;
    LoadingDialog customProgressDialog;

    private static final String TAG = "UserFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel homeViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        studentId = (TextView) root.findViewById(R.id.userInfoStudentId);
        studentName = (TextView) root.findViewById(R.id.userInfoStudentName);

        logout = (Button) root.findViewById(R.id.user_logout);
        passwordChange = (Button) root.findViewById(R.id.user_passwordChange);

        logout.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        getAssetService();
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
                timer.schedule(task, 3000);
            }
        });
        thread.start();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JwtToken.setToken(null);
                JwtToken.setId(null);
                getActivity().finish();
            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserPasswordChangeActivity.class);
                startActivity(intent);
            }
        });

        return root;
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
                    studentId.setText(result.getIdentifier());
                    studentName.setText(result.getOwner());

                    toast.cancel();
                    Toast.makeText(getContext(), "완료", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: 성공, 결과 \n" + result.toString());
                    customProgressDialog.cancel();
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
            public void onFailure(Call<UserGetAssetDTO> call, Throwable t) {
                Log.d(TAG,"onFailure" + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}