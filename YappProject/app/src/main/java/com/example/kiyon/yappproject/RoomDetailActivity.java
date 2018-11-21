package com.example.kiyon.yappproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.adapter.TaskListRVAdapter;
import com.example.kiyon.yappproject.common.CustomTypefaceSpan;
import com.example.kiyon.yappproject.common.OnDataChange;
import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.common.UserInfoReturn;
import com.example.kiyon.yappproject.model.Etc.BasicResponseResult;
import com.example.kiyon.yappproject.model.Room.RoomListResponseResult;
import com.example.kiyon.yappproject.model.Room.RoomAttendUsersItem;
import com.example.kiyon.yappproject.model.Task.TaskInfoItem;
import com.example.kiyon.yappproject.model.Task.TaskListResponseResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetailActivity extends AppCompatActivity {

    private static final String ROOM_DATA = "ROOM_DATA";

    private String roomCaptain_id; // 해당 Room 방장의 id 값
    private RecyclerView recyclerView;
    private TaskListRVAdapter taskListRVAdapter;

    private RoomListResponseResult roomListResponseResult;
    private ArrayList<RoomAttendUsersItem> roomAttendUsersItem;

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

        Initialize();
        loadTasksData(); // 과제 리스트 가져오기
    }

    private void Initialize() {

        // 인텐트 가져오기
        Intent intent = getIntent();
        roomListResponseResult = (RoomListResponseResult) intent.getSerializableExtra(ROOM_DATA); // 방 정보
        roomAttendUsersItem = roomListResponseResult.users; // 방에 참여한 유저 정보

        for (int i = 0 ; i < roomAttendUsersItem.size(); i++) { // 해당 room 방장의 id값을 알아내기 위한 코드
            if (roomAttendUsersItem.get(i).iscreater == 1) {
                roomCaptain_id = roomAttendUsersItem.get(i).kakao_id;
            }
        }

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_arrow_back);

        // recyclerview 셋팅
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskListRVAdapter = new TaskListRVAdapter(RoomDetailActivity.this, (ViewGroup) findViewById(R.id.container), roomCaptain_id);
        taskListRVAdapter.setOnClick(onItemClick);
        recyclerView.setAdapter(taskListRVAdapter);

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

        // click listener
        ImageView moreProfile = findViewById(R.id.moreProfile_iv);
        moreProfile.setOnClickListener(onClickListener);
        subject_add.setOnClickListener(onClickListener);
        room_done.setOnClickListener(onClickListener);

        // firebase 푸쉬메시지에서 오는 broadcast 등록
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("DataChange"));

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("test1414" , "실행");
            loadTasksData();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private OnDataChange onItemClick = new OnDataChange() {
        @Override
        public void onChange() {
            loadTasksData();
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.moreProfile_iv :
                    Intent intent = TeamMembersActivity.newIntent(RoomDetailActivity.this, roomAttendUsersItem);
                    startActivity(intent);
                    break;
                case R.id.subjectAdd_btn :
                    // 과제 추가 버튼 작업
                    Intent intent1 = AddTaskActivity.newIntent(RoomDetailActivity.this, roomAttendUsersItem);
                    startActivityForResult(intent1, 4000);
                    break;
                case R.id.roomDone_tv :
                    setDialogView();
                    break;
            }
        }
    };

    private void setDialogView() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_roomfinish);
        TextView text = dialog.findViewById(R.id.text);

        // 특정 텍스트 폰트 적용
        Typeface nanumBoldFont = Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("팀플을 완료하시겠습니까?완료 시 수정이 불가능합니다.");
        spannableStringBuilder.insert(13, "\n");
        spannableStringBuilder.setSpan (new CustomTypefaceSpan("", nanumBoldFont), 4, 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        text.setText(spannableStringBuilder);

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
                finishRoomToServer(roomListResponseResult.tm_code);
                dialog.dismiss();
            }
        });
    }

    private void finishRoomToServer(String tmcode) {

        Call<BasicResponseResult> call = RetrofitServerClient.getInstance().getService().RoomFinish(tmcode);
        call.enqueue(new Callback<BasicResponseResult>() {
            @Override
            public void onResponse(Call<BasicResponseResult> call, Response<BasicResponseResult> response) {
                if (response.isSuccessful()) {
                    BasicResponseResult basicResponseResult = response.body();
                    if (basicResponseResult != null) {
                        if (basicResponseResult.answer.equals("access")) {
                            // 방종료
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseResult> call, Throwable t) {

            }
        });
    }

    public void showProfileImage() {

        int roomMemberSize = roomAttendUsersItem.size();

        if (roomMemberSize >= 3) {
            if (roomAttendUsersItem.get(0).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image1);
            } else {
                Glide.with(RoomDetailActivity.this).load(roomAttendUsersItem.get(0).user_pic).into(profile_image1);
            }
            if (roomAttendUsersItem.get(1).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image2);
            } else {
                Glide.with(RoomDetailActivity.this).load(roomAttendUsersItem.get(1).user_pic).into(profile_image2);
            }
            if (roomAttendUsersItem.get(2).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image3);
            } else {
                Glide.with(RoomDetailActivity.this).load(roomAttendUsersItem.get(2).user_pic).into(profile_image3);
            }

        } else if (roomMemberSize == 2) {
            profile_image3.setVisibility(View.GONE);
            if (roomAttendUsersItem.get(0).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image1);
            } else {
                Glide.with(RoomDetailActivity.this).load(roomAttendUsersItem.get(0).user_pic).into(profile_image1);
            }
            if (roomAttendUsersItem.get(1).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image2);
            } else {
                Glide.with(RoomDetailActivity.this).load(roomAttendUsersItem.get(1).user_pic).into(profile_image2);
            }

        } else {
            profile_image2.setVisibility(View.GONE);
            profile_image3.setVisibility(View.GONE);
            if (roomAttendUsersItem.get(0).user_pic.equals("undefined")) {
                Glide.with(RoomDetailActivity.this).load(R.drawable.test_user).into(profile_image1);
            } else {
                Glide.with(RoomDetailActivity.this).load(roomAttendUsersItem.get(0).user_pic).into(profile_image1);
            }

        }
    }

    private void loadTasksData() {

        Call<TaskListResponseResult> call = RetrofitServerClient.getInstance().getService().TaskListResponseResult(roomListResponseResult.tm_code);
        call.enqueue(new Callback<TaskListResponseResult>() {
            @Override
            public void onResponse(Call<TaskListResponseResult> call, Response<TaskListResponseResult> response) {
                if (response.isSuccessful()) {
                    TaskListResponseResult taskListResponseResult = response.body();
                    if (taskListResponseResult != null) {
                        ArrayList<TaskInfoItem> taskInfoItems = taskListResponseResult.list;
                        if (taskInfoItems.size() != 0) { // 과제 목록이 없을 경우
                            taskListRVAdapter.setData(taskInfoItems);
                        } else {
                            findViewById(R.id.temp_iv).setVisibility(View.VISIBLE);
                            findViewById(R.id.temp_tv).setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskListResponseResult> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 4000 :
                    loadTasksData();
                    break;
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
