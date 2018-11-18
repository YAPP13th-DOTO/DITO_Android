package com.example.kiyon.yappproject.firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.kiyon.yappproject.MainActivity;
import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.common.UserInfoReturn;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

//    * 등록 토큰이 변할 경우
//    1. 앱에서 인스턴스 ID 삭제
//    2. 새 기기에서 앱 복원
//    3. 사용자가 앱 삭제/재설치
//    4. 사용자가 앱 데이터 소거
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d("test1414", "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }



    private void sendRegistrationToServer(String token) {


        String userID = UserInfoReturn.getInstance().getUserId(getApplicationContext());

        if (userID != null) {
            Call<ResponseBody> call = RetrofitServerClient.getInstance().getService().TokenResponseResult(token, userID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

        // 카카오 id랑 토큰값을 보내서 서버에 저장 (토큰값이 변경됬을 경우에도 문제없음.) 이때 카카오id가 null이라면 토큰값 보내면 안댐..
        // 그걸 mainactivity에서하자 왜냐하면 카카오id가 확정적으로 있기때문.

//        1. 첫 사용자가 어플을 설치할시
//        - 로그인하고 서버에 로그인 값들 전송
//        (토큰값을 같이 보낼순 없음 왜냐하면 로그인시점때 토큰값이 발행이 안될수도 있음. 테스트해봄)
//
//        - FirebaseInstanceIDService 클래스 onTokenRefresh() 시점에서 토큰값을 서버로 보내야댐..
//        (여기서 문제점은 아이디값을 sharedPreference로 뽑아와서 보내야되는데 이때 사용자가 로그인을 한 이력
//        이 없고 그냥 계속 로그인화면일 경우 로그인정보가 없을 경우도 있음 ..)
//        해결 방법 : MainActivity에서는 로그인이 확실하게 된 상황이니 이때 토큰값이 있다면 서버로 보내고 없다면
//        onTokenRefresh()에서 카카오id를 조사후 있다면 서버로 전송해서 문제 발생할 여지가 없을꺼같음.
//
//        - 또다른 문제가
//
//        2. 기존 사용자가 어플을 켰는데 토큰값이 변경됬을경우
//        - 단순하게 onTokenRefrsh에서 서버로 토큰값을 전송해서 서버에서 기존값이랑 변경시키면 될듯..
    }

}
