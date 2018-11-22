package com.team_yapp.kiyon.yappproject.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.team_yapp.kiyon.yappproject.R;
import com.team_yapp.kiyon.yappproject.adapter.TeamMembersRVAdapter;
import com.team_yapp.kiyon.yappproject.common.StatusBarColorChange;
import com.team_yapp.kiyon.yappproject.model.Room.RoomAttendUsersItem;

import java.util.ArrayList;

public class TeamMembersActivity extends AppCompatActivity {

    private static final String TEAM_DATA = "TEAM_DATA";

    public static Intent newIntent(Context context, ArrayList<RoomAttendUsersItem> lists) {
        Intent intent = new Intent(context, TeamMembersActivity.class);
        intent.putExtra(TEAM_DATA, lists);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_members);

        // 상태바 셋팅
        StatusBarColorChange.setStatusBarColor(TeamMembersActivity.this, getResources().getColor(R.color.white));

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_arrow_back);

        // recyclerview 셋팅
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TeamMembersRVAdapter teamMembersRVAdapter = new TeamMembersRVAdapter(TeamMembersActivity.this);
        recyclerView.setAdapter(teamMembersRVAdapter);

        // 인텐트 정보 가져오기
        Intent intent = getIntent();
        ArrayList<RoomAttendUsersItem> lists = (ArrayList<RoomAttendUsersItem>) intent.getSerializableExtra(TEAM_DATA);

        // 유저정보 - > recyclerview adapter
        teamMembersRVAdapter.setData(lists);



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
