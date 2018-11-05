package com.example.kiyon.yappproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.model.RoomList.RoomListResponseResult;
import com.example.kiyon.yappproject.model.RoomList.UserResponseResult;

import java.util.ArrayList;

public class RoomDetailActivity extends AppCompatActivity {

    private static final String ROOM_DATA = "ROOM_DATA";

    private RoomListResponseResult roomListResponseResult;
    private ArrayList<UserResponseResult> userResponseResult;

    private RelativeLayout head_layout;
    private TextView toolbarTitle_tv;

    private ImageView profile_image1;
    private ImageView profile_image2;
    private ImageView profile_image3;

    public static Intent newIntent(Context context, RoomListResponseResult roomListResponseResult) {
        Intent intent = new Intent(context, RoomDetailActivity.class);
        intent.putExtra(ROOM_DATA, roomListResponseResult);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        // 인텐트 가져오기
        Intent intent = getIntent();
        roomListResponseResult = (RoomListResponseResult) intent.getSerializableExtra(ROOM_DATA); // 방 정보
        userResponseResult = roomListResponseResult.users; // 방에 참여한 유저 정보

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_arrow_back);

        // 툴바 제목
        toolbarTitle_tv = findViewById(R.id.title_toolbar);
        toolbarTitle_tv.setText(roomListResponseResult.tm_name);

        //상태바 색상
        setStatusBarColor(RoomDetailActivity.this, getResources().getColor(R.color.yellow));

        head_layout = findViewById(R.id.head_layout);
        AppBarLayout appBarLayout = findViewById(R.id.appBar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 1.5) {
                    toolbarTitle_tv.setVisibility(View.VISIBLE);
                    head_layout.setVisibility(View.GONE);
                } else {
                    toolbarTitle_tv.setVisibility(View.GONE);
                    head_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        // 방, 과목 이름 셋팅
        TextView title_tv = findViewById(R.id.roomName);
        title_tv.setText(roomListResponseResult.tm_name);
        TextView subject_tv = findViewById(R.id.subjectName);
        subject_tv.setText(roomListResponseResult.sub_name);

        // 참여자 이미지 셋팅
        profile_image1 = findViewById(R.id.profile_image1);
        profile_image2 = findViewById(R.id.profile_image2);
        profile_image3 = findViewById(R.id.profile_image3);

        // 프로필 이미지 셋팅
        showProfileImage();

        TextView room_done = findViewById(R.id.roomDone_tv);
        Button subject_add = findViewById(R.id.subjectAdd_btn);

        // 방장이 아닐 경우 제출,완료 버튼 삭제
        if (roomListResponseResult.iscreater != 1) {
            room_done.setVisibility(View.GONE);
            subject_add.setVisibility(View.GONE);
        }

        ImageView moreProfile = findViewById(R.id.moreProfile_iv);
        moreProfile.setOnClickListener(onClickListener);


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.moreProfile_iv :
                    Intent intent = TeamMembersActivity.newIntent(RoomDetailActivity.this, userResponseResult);
                    startActivity(intent);
                    break;
                case R.id.subjectAdd_btn :
                    // 과제 추가 버튼 작업
                    Intent intent1 = AddTaskActivity.newIntent(RoomDetailActivity.this, userResponseResult);
                    startActivity(intent1);
                    break;
                case R.id.roomDone_tv :
                    // 방 완료 버튼 작업
                    break;
            }
        }
    };

    public void showProfileImage() {

        int roomMemberSize = userResponseResult.size();

        if (roomMemberSize >= 3) {
            if (userResponseResult.get(0).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image1);
            } else {
                Glide.with(RoomDetailActivity.this).load(userResponseResult.get(0).user_pic).into(profile_image1);
            }
            if (userResponseResult.get(1).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image2);
            } else {
                Glide.with(RoomDetailActivity.this).load(userResponseResult.get(1).user_pic).into(profile_image2);
            }
            if (userResponseResult.get(2).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image3);
            } else {
                Glide.with(RoomDetailActivity.this).load(userResponseResult.get(2).user_pic).into(profile_image3);
            }

        } else if (roomMemberSize == 2) {
            profile_image3.setVisibility(View.GONE);
            Log.d("tete123", "실행1");
            if (userResponseResult.get(0).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image1);
            } else {
                Glide.with(RoomDetailActivity.this).load(userResponseResult.get(0).user_pic).into(profile_image1);
            }
            if (userResponseResult.get(1).user_pic.equals("undefined")) {
                Log.d("tete123", "실행2");
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image2);
            } else {
                Log.d("tete123", "실행3");
                Glide.with(RoomDetailActivity.this).load(userResponseResult.get(1).user_pic).into(profile_image2);
            }

        } else {
            profile_image2.setVisibility(View.GONE);
            profile_image3.setVisibility(View.GONE);
            if (userResponseResult.get(0).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image1);
            } else {
                Glide.with(RoomDetailActivity.this).load(userResponseResult.get(0).user_pic).into(profile_image1);
            }

        }
    }

    public void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
