package com.example.hyperledgervirtualmoneyproject.ui.user;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hyperledgervirtualmoneyproject.API.UserApi;
import com.example.hyperledgervirtualmoneyproject.DTO.ErrorBody;
import com.example.hyperledgervirtualmoneyproject.DTO.JwtToken;
import com.example.hyperledgervirtualmoneyproject.DTO.UserLoginDTO;
import com.example.hyperledgervirtualmoneyproject.MainActivity;
import com.example.hyperledgervirtualmoneyproject.R;
import com.example.hyperledgervirtualmoneyproject.UserCreateActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPasswordChangeActivity extends AppCompatActivity {

    EditText password, rePassword;
    Button confirm, cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordchange);

        password = (EditText) findViewById(R.id.pwChangeEdit);
        rePassword = (EditText) findViewById(R.id.pwChangeEditRe);
        confirm = (Button) findViewById(R.id.pwChangeConfirm);
        cancel = (Button) findViewById(R.id.pwChangeCancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "password: \n" + password.getText());
                Log.d(TAG, "rePassword: \n" + rePassword.getText());
                if(password.getText().toString().equals(rePassword.getText().toString())){
                    userChangePassword(password.getText().toString());
                }
                else{
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주헤요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void userChangePassword(String newPassword){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);

        Call<Void> call = service.changePassword(JwtToken.getJwt(), newPassword);
        System.out.println("password = " + password);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "비밀번호 수정에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    Log.d(TAG, "onResponse: 성공, 결과 \n");
                }else{
                    Log.d(TAG, "onResponse: 실패");
                    Toast.makeText(getApplicationContext(), "비밀번호 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    try {
                        String errorMessage = response.errorBody().string();
                        ObjectMapper objectMapper = new ObjectMapper();
                        ErrorBody errorBody = objectMapper.readValue(errorMessage, ErrorBody.class);
                        if (errorBody.getMessage().equals("잘못된 식별 번호로 요청했습니다")) {
                            Toast.makeText(getApplicationContext(), "다시 로그인 해주세요", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG,"onFailure" + t.getMessage());
                Toast.makeText(getApplicationContext(), "서버와 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
