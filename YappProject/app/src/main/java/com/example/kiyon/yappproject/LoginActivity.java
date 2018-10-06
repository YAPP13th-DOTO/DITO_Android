package com.example.kiyon.yappproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.model.LoginResponseResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kakao.util.helper.Utility.getPackageInfo;


public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    private ImageView btn_kakao_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);


        btn_kakao_login= findViewById(R.id.btn_kakao_login);
        btn_kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Session session = Session.getCurrentSession();
                session.addCallback(new SessionCallback());
                session.open(AuthType.KAKAO_ACCOUNT , LoginActivity.this);
            }
        });

        loginSucess();

    }

    //    private void getHashKey() {
//        try {
//            PackageInfo info = getPackageInfo(LoginActivity.this, PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("HASH_KEY", "key_hash=" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }



    public class SessionCallback implements ISessionCallback {


        // 로그인에 성공한 상태

        @Override

        public void onSessionOpened() {

            requestMe();

        }


        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {

            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());

        }


        // 사용자 정보 요청
        public void requestMe() {

            // 사용자정보 요청 결과에 대한 Callback

            UserManagement.requestMe(new MeResponseCallback() {

                // 세션 오픈 실패. 세션이 삭제된 경우,

                @Override

                public void onSessionClosed(ErrorResult errorResult) {

                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());

                }


                // 회원이 아닌 경우,

                @Override

                public void onNotSignedUp() {

                    Log.e("SessionCallback :: ", "onNotSignedUp");

                }


                // 사용자정보 요청에 성공한 경우,

                @Override

                public void onSuccess(UserProfile userProfile) {


                    Log.e("SessionCallback :: ", "onSuccess");

                    String nickname = userProfile.getNickname();

                    String email = userProfile.getEmail();

                    String profileImagePath = userProfile.getProfileImagePath();

                    String thumnailPath = userProfile.getThumbnailImagePath();

                    String UUID = userProfile.getUUID();

                    long id = userProfile.getId();

//
//                    Log.e("Profile : ", nickname + "");
//
//                    Log.e("Profile : ", email + "");
//
//                    Log.e("Profile : ", profileImagePath + "");
//
//                    Log.e("Profile : ", thumnailPath + "");
//
//                    Log.e("Profile : ", UUID + "");
//
//                    Log.e("Profile : ", id + "");

                    Call<LoginResponseResult> call = RetrofitServerClient.getInstance().getService().LoginResponseResult(UUID, nickname, profileImagePath);
                    Log.d("test123", String.valueOf(call.request().url()));
                    call.enqueue(new Callback<LoginResponseResult>() {
                        @Override
                        public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {

                            if (response.isSuccessful()) {
                                LoginResponseResult loginResponseResult = response.body();
                                if (loginResponseResult != null) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("DITO", MODE_PRIVATE);
                                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userID", loginResponseResult.kakao_id);
                                    editor.putString("userName", loginResponseResult.user_name);
                                    editor.putString("userImage", loginResponseResult.user_pic);
                                    editor.apply();

                                    loginSucess();
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                            Log.d("test123", t.getMessage());

                        }
                    });

                }

                // 사용자 정보 요청 실패

                @Override
                public void onFailure(ErrorResult errorResult) {

                    Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());

                }

            });

            UserManagement.requestLogout(new LogoutResponseCallback() {

                @Override

                public void onCompleteLogout() {
/*
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);*/

                }

            });

        }

    }

    public void loginSucess() {
        // 저장된 토큰값 가져오기
        Log.d("test123", "로그인 성공");
        SharedPreferences sharedPreferences = getSharedPreferences("DITO", MODE_PRIVATE);
        String userID = sharedPreferences.getString("userID", null);
        String userName = sharedPreferences.getString("userName", null);
        String userImage = sharedPreferences.getString("userImage", null);

        Log.d("test123", userID + userName + userImage);
        if (userID != null && userName != null && userImage != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
