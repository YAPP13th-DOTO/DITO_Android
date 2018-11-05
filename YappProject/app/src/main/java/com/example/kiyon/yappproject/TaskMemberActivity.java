package com.example.kiyon.yappproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kiyon.yappproject.adapter.MemberListRVAdapter;
import com.example.kiyon.yappproject.model.RoomList.UserResponseResult;

import java.util.ArrayList;

import static com.example.kiyon.yappproject.AddTaskActivity.USER_DATA;

public class TaskMemberActivity extends AppCompatActivity {
    RecyclerView recyclerView1,recyclerView2;

    public static Intent newIntent (Context context, ArrayList<UserResponseResult> list) {
        Intent intent = new Intent(context, TaskMemberActivity.class);
        intent.putExtra(USER_DATA,list);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_member);

        //팀원 전체
        recyclerView2 = findViewById(R.id.recyclerview2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        MemberListRVAdapter memberListRVAdapter = new MemberListRVAdapter(TaskMemberActivity.this);
        recyclerView2.setAdapter(memberListRVAdapter);
    }
    public void onClickTaskMember(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                break;
        }
    }
}
