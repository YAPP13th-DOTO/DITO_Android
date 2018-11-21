package com.example.kiyon.yappproject.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.adapter.AttendTeamMemberRVAdapter;
import com.example.kiyon.yappproject.adapter.MemberListRVAdapter;
import com.example.kiyon.yappproject.common.StatusBarColorChange;
import com.example.kiyon.yappproject.model.Room.RoomAttendUsersItem;

import java.util.ArrayList;

import static com.example.kiyon.yappproject.view.TaskCreateActivity.USER_DATA;

public class TaskMemberActivity extends AppCompatActivity implements MemberListRVAdapter.ItemClickListener {

    private LinearLayout recyclerview_Layout;
    private RecyclerView recyclerView1,recyclerView2;
    private MemberListRVAdapter memberListRVAdapter;
    private ArrayList<RoomAttendUsersItem> attendUserLists = new ArrayList<>();
    private ArrayList<RoomAttendUsersItem> totalUserLists = new ArrayList<>();

    public static Intent newIntent (Context context, ArrayList<RoomAttendUsersItem> list) {
        Intent intent = new Intent(context, TaskMemberActivity.class);
        intent.putExtra(USER_DATA,list);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_member);

        // 상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(TaskMemberActivity.this, getResources().getColor(R.color.yellow));

        //팀원 전체
        recyclerView2 = findViewById(R.id.recyclerview2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        memberListRVAdapter = new MemberListRVAdapter(TaskMemberActivity.this);
        memberListRVAdapter.setClickListener(this);
        recyclerView2.setAdapter(memberListRVAdapter);

        Intent intent = getIntent();
        totalUserLists = (ArrayList<RoomAttendUsersItem>) intent.getSerializableExtra(USER_DATA);


        memberListRVAdapter.setData(totalUserLists);

        //선탣된 팀원
        recyclerview_Layout = findViewById(R.id.recyclerview_Layout);
        recyclerView1 = findViewById(R.id.recyclerview1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


    }

    public void onClickTaskMember(View v) {
        switch (v.getId()) {
            case R.id.backBtn:      //<- 버튼 눌렀을 때
                sendUserListToAddTaskActivity();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        sendUserListToAddTaskActivity();
        super.onBackPressed();
    }

    private void sendUserListToAddTaskActivity() {
        Intent intent = new Intent(TaskMemberActivity.this, TaskCreateActivity.class);
        intent.putExtra("users", attendUserLists);
        intent.putExtra("result", attendUserLists.size() + "명");
        setResult(RESULT_OK,intent);
    }

    @Override
    public void onItemClick(int pos, boolean check, RoomAttendUsersItem person) {

        if(check) {
            attendUserLists.add(person);
            if (attendUserLists.size() != 0) {
                recyclerview_Layout.setVisibility(View.VISIBLE);
            }
        }
        else {
            attendUserLists.remove(person);
            if (attendUserLists.size() == 0 ) {
                recyclerview_Layout.setVisibility(View.GONE);
            }
        }

        AttendTeamMemberRVAdapter attendTeamMemberRVAdapter = new AttendTeamMemberRVAdapter(attendUserLists,TaskMemberActivity.this);
        recyclerView1.setAdapter(attendTeamMemberRVAdapter);

    }

}
