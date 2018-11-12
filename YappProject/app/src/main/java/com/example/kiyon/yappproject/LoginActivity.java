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
import android.widget.Toast;


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
import com.kakao.util.protocol.KakaoProtocolService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kakao.util.helper.Utility.getPackageInfo;


public class LoginActivity extends AppCompatActivity {

    private SessionCallback callback;
    private ImageView btn_kakao_login;

    private String nickname;

    private String profileImagePath;

    private  String uid;

    private boolean isChecked = true;

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
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorCode());
                    if (errorResult.getErrorMessage().equals("this access token does not exist")) {
                        Toasty.warning(LoginActivity.this , "다시 한번 시도해주세요.", Toast.LENGTH_LONG).show();
                    }
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

                    nickname = userProfile.getNickname();
                    profileImagePath = userProfile.getProfileImagePath();
                    uid = String.valueOf(userProfile.getId());
                    userProfile.getUUID()
                    if (isChecked) {
                        isChecked = false;
                        loadData(uid, nickname, profileImagePath);
                    }


//                    long id = userProfile.getId();
//                    String thumnailPath = userProfile.getThumbnailImagePath();
//                    String email = userProfile.getEmail();

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

                }

            });

        }

    }
    public void loadData(final String uid, final String nickname, final String profileImagePath) {
        if (uid != null) {
            Call<LoginResponseResult> call = RetrofitServerClient.getInstance().getService().LoginResponseResult(uid, nickname, profileImagePath);
            Log.d("test1414", String.valueOf(call.request().url()));
            call.enqueue(new Callback<LoginResponseResult>() {
                @Override
                public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {
                    if (response.isSuccessful()) {
                        LoginResponseResult loginResponseResult = response.body();
                        if (loginResponseResult != null) {
                            if (loginResponseResult.answer.equals("success")) {
                                SharedPreferences sharedPreferences = getSharedPreferences("DITO", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userID", uid);
                                editor.putString("userName", nickname);
                                editor.putString("userImage", profileImagePath);
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                isChecked = true;
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                    Log.d("errorMsg", t.getMessage());

                }
            });
        } else {
            // 카카오 로그인이 실패 했을 경우
        }
    }
}
