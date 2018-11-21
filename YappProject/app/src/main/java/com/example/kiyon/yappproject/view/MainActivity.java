package com.example.kiyon.yappproject.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.adapter.RoomListRVAdapter;
import com.example.kiyon.yappproject.common.CustomTypefaceSpan;
import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.common.StatusBarColorChange;
import com.example.kiyon.yappproject.common.UserInfoReturn;
import com.example.kiyon.yappproject.model.Etc.BasicResponseResult;
import com.example.kiyon.yappproject.model.Room.RoomListResponseResult;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FloatingActionsMenu addBtn;
    private RecyclerView recyclerView;
    private RoomListRVAdapter roomListRVAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // 상태바 셋팅
        StatusBarColorChange.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.white));

        addBtn = findViewById(R.id.addBtn);
        Animation addBtnOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.addbtn_open);
        Animation addBtnClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.addbtn_close);

        FloatingActionButton roomMakeBtn = findViewById(R.id.roomMakeBtn);
        FloatingActionButton roomGoBtn = findViewById(R.id.roomGoBtn);
        roomMakeBtn.setOnClickListener(onClickListener);
        roomGoBtn.setOnClickListener(onClickListener);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomListRVAdapter = new RoomListRVAdapter(this);
        recyclerView.setAdapter(roomListRVAdapter);

        dialog = new Dialog(this);

        // pull to refresh 사용
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        // 방 리스트 가져오기
        loadRoomData();
        // 토큰값 서버로 전송
        sendFCMTokenToServer();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        loadRoomData();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.roomMakeBtn :
                    Intent intent = RoomCreateActivity.newIntent(MainActivity.this);
                    startActivity(intent);
                    addBtn.collapse();
                    break;
                case R.id.roomGoBtn :
                    setDialogView();
                    addBtn.collapse();
                    break;
            }
        }
    };


    public void setDialogView() {

        dialog.setContentView(R.layout.dialog_room_attend);

        TextView test_tv = dialog.findViewById(R.id.content_tv);
        final EditText roomCode_edit = dialog.findViewById(R.id.roomcode_edit);

        // 특정 텍스트 폰트 적용
        Typeface nanumBoldFont = Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("팀플 참여코드를입력하세요.");
        spannableStringBuilder.insert(8, "\n");
        spannableStringBuilder.setSpan (new CustomTypefaceSpan("", nanumBoldFont), 3, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        test_tv.setText(spannableStringBuilder);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dialog.findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.ok_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = UserInfoReturn.getInstance().getUserId(MainActivity.this);

                String roomCode = roomCode_edit.getText().toString();

                attendRoom(userID, roomCode);
            }
        });

    }

    // 방 참여
    private void attendRoom(String userId, String roomCode) {
        Call<BasicResponseResult> call = RetrofitServerClient.getInstance().getService().RoomAttend(roomCode, userId);
        call.enqueue(new Callback<BasicResponseResult>() {
            @Override
            public void onResponse(Call<BasicResponseResult> call, Response<BasicResponseResult> response) {
                if (response.isSuccessful()) {
                    BasicResponseResult basicResponseResult = response.body();
                    if (basicResponseResult != null) {
                        if (basicResponseResult.answer.equals("access")) { // 참여 성공
                            loadRoomData();
                            dialog.dismiss();
                        } else if (basicResponseResult.answer.equals("already")) {
                            Toasty.warning(MainActivity.this, "이미 참여중인 방입니다.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else { // 참여 실패(이미 참여한 방)
                            Toasty.error(MainActivity.this, "방 코드를 다시 한번 확인해주세요.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseResult> call, Throwable t) {
                Log.d("결과값", t.getMessage());

            }
        });
    }

    // 로그인유저 토큰값 서버로 전송
    private void sendFCMTokenToServer() {

        String token = FirebaseInstanceId.getInstance().getToken();

        String userID = UserInfoReturn.getInstance().getUserId(MainActivity.this);

        if (userID != null && token != null) {
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

    }

    // 방 목록 가져오기
    private void loadRoomData() {

        String userID = UserInfoReturn.getInstance().getUserId(MainActivity.this);

        Call<ArrayList<RoomListResponseResult>> call = RetrofitServerClient.getInstance().getService().RoomListResponseResult(userID);
        call.enqueue(new Callback<ArrayList<RoomListResponseResult>>() {
            @Override
            public void onResponse(Call<ArrayList<RoomListResponseResult>> call, Response<ArrayList<RoomListResponseResult>> response) {
                if (response.isSuccessful()) {
                    ArrayList<RoomListResponseResult> list = response.body();
                    if (list != null) {
                        findViewById(R.id.default_layout).setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        roomListRVAdapter.setRoomData(list);
                        swipeRefreshLayout.setRefreshing(false); // 리플레쉬 종료
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RoomListResponseResult>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onRefresh() {
        loadRoomData();
    }
}
