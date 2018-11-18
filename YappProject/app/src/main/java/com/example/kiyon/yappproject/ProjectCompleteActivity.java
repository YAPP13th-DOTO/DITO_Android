package com.example.kiyon.yappproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kiyon.yappproject.adapter.ProjectCompleteRVAdapter;
import com.example.kiyon.yappproject.model.Room.RoomAttendUsersItem;
import com.example.kiyon.yappproject.model.Task.TaskInfoItem;

import java.util.ArrayList;

public class ProjectCompleteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProjectCompleteRVAdapter projectCompleteRVAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context,ProjectCompleteActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_complete);

        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(projectCompleteRVAdapter);
    }
}
