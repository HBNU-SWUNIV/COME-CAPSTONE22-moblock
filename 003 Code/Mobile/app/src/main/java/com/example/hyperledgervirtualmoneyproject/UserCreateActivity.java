package com.example.hyperledgervirtualmoneyproject;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hyperledgervirtualmoneyproject.API.UserApi;
import com.example.hyperledgervirtualmoneyproject.DTO.JwtToken;
import com.example.hyperledgervirtualmoneyproject.DTO.UserCreateBodyDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserLoginDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type User create activity.
 */
public class UserCreateActivity extends AppCompatActivity {

    LinearLayout passwordLayout, idLayout;
    EditText studentId, password, name;
    Button confirm, cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercreate);

        studentId = (EditText) findViewById(R.id.studentId);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        confirm = (Button) findViewById(R.id.userCreateConfirm);
        cancel = (Button) findViewById(R.id.userCreateCancel);
        passwordLayout = (LinearLayout) findViewById(R.id.create_password_layout);
        idLayout = (LinearLayout) findViewById(R.id.create_id_layout);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordString = password.getText().toString();
                String IdString = studentId.getText().toString();
                //최소 하나의 숫자와 문자, 최소 8글자 이상 입력
                Pattern PwPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
                Pattern IPattern = Pattern.compile("^[0-9]{8}");

                //비밀번호 정규식 체크
                boolean passwordCheck = PwPattern.matcher(passwordString).find();

                //ID 정규식 체크
                boolean idCheck = IPattern.matcher(IdString).find();

                Log.d(TAG,IdString + String.valueOf(idCheck));

                if (passwordCheck && idCheck) {
                    passwordLayout.setBackground(getDrawable(R.drawable.text_view_shape));
                    idLayout.setBackground(getDrawable(R.drawable.text_view_shape));
                    createUser(IdString, passwordString, name.getText().toString(), "ROLE_STUDENT");
                }else{
                    if(!passwordCheck) {
                        passwordLayout.setBackground(getDrawable(R.drawable.text_view_led_shape));
                    }else{
                        passwordLayout.setBackground(getDrawable(R.drawable.text_view_shape));
                    }
                    if (!idCheck) {
                        idLayout.setBackground(getDrawable(R.drawable.text_view_led_shape));
                    }else{
                        idLayout.setBackground(getDrawable(R.drawable.text_view_shape));
                    }
                    Toast.makeText(getApplicationContext(), "형식에 맞춰주세요", Toast.LENGTH_SHORT).show();
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


    /**
     * Create user.
     *
     * step1: retrofit 설정
     * step2: API 요청
     * step3: response 데이터를 받아 메모리에 저장
     *
     * @param identifier the identifier
     * @param password   the password
     * @param name       the name
     * @param role       the role
     */
    public void createUser(String identifier, String password, String name, String role){
        //step1
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);

        UserCreateBodyDTO userCreateBodyDTO = new UserCreateBodyDTO(identifier, password, name, role);

        Call<UserLoginDTO> call = service.join(userCreateBodyDTO);

        //step2
        call.enqueue(new Callback<UserLoginDTO>() {
            @Override
            public void onResponse(Call<UserLoginDTO> call, Response<UserLoginDTO> response) {
                if(response.isSuccessful()){
                    //step3
                    System.out.println("response.body(); = " + response.body().toString());
                    UserLoginDTO result = response.body();
                    Toast.makeText(getApplicationContext(), "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    JwtToken.setToken(result.getAccessToken());
                    Log.d(TAG, "onResponse: 성공, 결과 \n" + result.toString());
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<UserLoginDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 연결에  실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"onFailure" + t.getMessage());
            }
        });
    }
}
