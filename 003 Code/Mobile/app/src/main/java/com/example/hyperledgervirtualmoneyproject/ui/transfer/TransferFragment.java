package com.example.hyperledgervirtualmoneyproject.ui.transfer;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.hyperledgervirtualmoneyproject.API.UserApi;
import com.example.hyperledgervirtualmoneyproject.API.UserTradeApi;
import com.example.hyperledgervirtualmoneyproject.DTO.ErrorBody;
import com.example.hyperledgervirtualmoneyproject.DTO.JwtToken;
import com.example.hyperledgervirtualmoneyproject.DTO.QrCreateDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserGetAssetDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserTransferDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserTransferRequestDTO;
import com.example.hyperledgervirtualmoneyproject.LoadingDialog;
import com.example.hyperledgervirtualmoneyproject.MainActivity;
import com.example.hyperledgervirtualmoneyproject.R;
import com.example.hyperledgervirtualmoneyproject.databinding.FragmentTransferBinding;
import com.example.hyperledgervirtualmoneyproject.ui.home.HomeFragment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransferFragment extends Fragment {

    TextView amountHint;
    EditText receiver, coin, amount;
    Button confirm, qrRead;
    Spinner coinSpinner;
    String[] coinList;
    HashMap<String, String> coinMap;
    String[] coinValues;


    private FragmentTransferBinding binding;
    private IntentIntegrator qrScan;
    private LoadingDialog customProgressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TransferViewModel dashboardViewModel =
                new ViewModelProvider(this).get(TransferViewModel.class);

        binding = FragmentTransferBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        receiver = (EditText) root.findViewById(R.id.receiver);
        coin = (EditText) root.findViewById(R.id.coin);
        amount = (EditText) root.findViewById(R.id.amount);
        confirm = (Button) root.findViewById(R.id.userTransferConfirm);
        qrRead = (Button) root.findViewById(R.id.userTransferReadQr);
        coinSpinner = (Spinner) root.findViewById(R.id.spinner_coin);
        amountHint = (TextView) root.findViewById(R.id.amountHint);

        getAssetService();

        customProgressDialog = new LoadingDialog(getContext());
        //???????????? ????????????
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

        coinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                coinMap.get(coinList[i]);
                coin.setText(coinList[i]);
                amountHint.setHint("??????: "+ coinValues[i] + "???");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                coin.setText("????????? ????????? ?????????");
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserTransferRequestDTO userTransferRequestDTO = new UserTransferRequestDTO(
                        receiver.getText().toString(),
                        coin.getText().toString(),
                        Long.parseLong(amount.getText().toString())
                );
                coinTransferService(v, JwtToken.getJwt(), userTransferRequestDTO);
            }
        });

        qrRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan = IntentIntegrator.forSupportFragment(TransferFragment.this);
                qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Coin transfer service.
     *
     * step1: retrofit ??????
     * step2: API ??????
     * step3: API ????????? ????????? ?????? ????????? ??????
     *
     * exception: jwt????????? ??????????????? ??????????????? ?????? ???????????? ??????
     *
     * @param v                      the v
     * @param jwt                    the jwt
     * @param userTransferRequestDTO the user transfer request dto
     */
    public void coinTransferService(View v, String jwt, UserTransferRequestDTO userTransferRequestDTO){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("??????????????????..");
        progressDialog.show();

        //step1
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserTradeApi service = retrofit.create(UserTradeApi.class);
        Call<UserTransferDTO> call = service.transfer(jwt, userTransferRequestDTO);


        //step2
        call.enqueue(new Callback<UserTransferDTO>() {
            @Override
            public void onResponse(Call<UserTransferDTO> call, Response<UserTransferDTO> response) {
                //step3
                if(response.isSuccessful()){
                    UserTransferDTO result = response.body();
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: ??????, ?????? \n" + result.toString());

                    AlertDialog.Builder successDialog = new AlertDialog.Builder(getContext());
                    successDialog.setMessage("????????? ?????????????????????.")
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    MainActivity mainActivity = (MainActivity) getActivity();
//                                    mainActivity.getSupportFragmentManager().beginTransaction()
//                                            .replace(R.id.container, new HomeFragment()).commit();
                                }
                            }).create().show();
                }else{
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: ??????");
                    AlertDialog.Builder successDialog = new AlertDialog.Builder(getContext());
                    successDialog.setMessage("????????? ??????????????????.")
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    MainActivity mainActivity = (MainActivity) getActivity();
//                                    mainActivity.getSupportFragmentManager().beginTransaction()
//                                            .replace(R.id.container, new HomeFragment()).commit();
                                }
                            }).create().show();
                    try {
                        //exception
                        String errorMessage = response.errorBody().string();
                        ObjectMapper objectMapper = new ObjectMapper();
                        ErrorBody errorBody = objectMapper.readValue(errorMessage, ErrorBody.class);
                        if (errorBody.getMessage().equals("????????? ?????? ????????? ??????????????????")) {
                            Toast.makeText(getContext(), "?????? ????????? ????????????", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserTransferDTO> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"onFailure" + t.getMessage());
            }
        });
    }


    /**
     * QR Code Scan
     *
     * step1: QR ?????? ??????
     * step2: ??????????????? ObjectMapper??? ???????????? DTO??? ??????
     * step3: Coin ????????? ?????? ????????? Selection ??????
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //step1
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //result.getContents??? ?????? ??????
                Toast.makeText(getContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                try {
                    // JSONParser??? JSONObject??? ??????, step2
                    ObjectMapper objectMapper = new ObjectMapper();
                    QrCreateDTO qrCreateDTO = objectMapper.readValue(result.getContents(), QrCreateDTO.class);
                    receiver.setText(qrCreateDTO.getReceiverId().toString());
                    coin.setText(qrCreateDTO.getCoinName());
                    amount.setText(qrCreateDTO.getAmount().toString());

                    //step3
                    for (int i = 0; i < coinList.length; i++) {
                        if (coinList[i].equals(qrCreateDTO.getCoinName())) {
                            coinSpinner.setSelection(i);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Get asset service.
     *
     * step1: ???????????? ??????
     * step2: API ??????
     * step3: response ???????????? ?????? ???????????? ????????? ???????????? ??????
     *
     * exception: jwt????????? ??????????????? ??????????????? ?????? ???????????? ??????
     *
     */
    public void getAssetService(){
        //step1
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);

        System.out.println("jwtToken = " + JwtToken.getJwt());
        Call<UserGetAssetDTO> call = service.getAsset(JwtToken.getJwt());

        Toast toast = Toast.makeText(getContext(), "?????? ????????? ???????????? ???...", Toast.LENGTH_LONG);
        toast.show();

        //step2
        call.enqueue(new Callback<UserGetAssetDTO>() {
            @Override
            public void onResponse(Call<UserGetAssetDTO> call, Response<UserGetAssetDTO> response) {
                if(response.isSuccessful()){
                    //step3
                    UserGetAssetDTO result = response.body();
                    coinMap = result.getCoin();
                    coinList = coinMap.keySet().toArray(new String[coinMap.size()]);
                    coinValues = coinMap.values().toArray(new String[coinMap.size()]);

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_layout, coinList);
                    coinSpinner.setAdapter(spinnerAdapter);

                    customProgressDialog.cancel();
                }else{
                    Toast.makeText(getContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: ??????");
                    try {
                        //exception
                        String errorMessage = response.errorBody().string();
                        ObjectMapper objectMapper = new ObjectMapper();
                        ErrorBody errorBody = objectMapper.readValue(errorMessage, ErrorBody.class);
                        if (errorBody.getMessage().equals("????????? ?????? ????????? ??????????????????")) {
                            Toast.makeText(getContext(), "?????? ????????? ????????????", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserGetAssetDTO> call, Throwable t) {
                Toast.makeText(getContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"onFailure" + t.getMessage());
            }
        });
    }
}