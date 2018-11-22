package com.example.kiyon.yappproject.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.common.UserInfoReturn;
import com.example.kiyon.yappproject.model.Etc.LoginResponseResult;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
//
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("test1414", "Refreshed token: " + refreshedToken);

        handler.postDelayed(runnable, 1000);



    }

    public void loadData() {

        String userID = UserInfoReturn.getInstance().getUserId(SplashActivity.this);

        if (userID == null) { // 휴대폰에 로그인 기록이 없을 경우
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else { // 기록이 있을 경우
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
                                finish();
                            } else { // 유저가 로그인을 한적이 없거나 서버에 유저 정보값이 저장되어 있지 않을때
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                    // 서버 에러
                    Toasty.error(getApplicationContext(), "서버 오류" , Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
