package com.example.kiyon.yappproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.model.LoginResponseResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

//    Handler handler = new Handler();
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

//        handler.postDelayed(runnable, 1500);

        loadData();

    }

    public void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("DITO", MODE_PRIVATE);
        String userID = sharedPreferences.getString("userID", null);

        Call<LoginResponseResult> call = RetrofitServerClient.getInstance().getService().ConfirmResponseResult(userID);
        call.enqueue(new Callback<LoginResponseResult>() {
            @Override
            public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {
                if (response.isSuccessful()) {
                    LoginResponseResult loginResponseResult = response.body();
                    if (loginResponseResult != null) {
                        if (loginResponseResult.answer.equals("access")) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else { // 유저가 로그인을 한적이 없거나 서버에 유저 정보값이 저장되어 있지 않을때
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                Log.d("SplashActivity", t.getMessage());
            }
        });
    }
}
