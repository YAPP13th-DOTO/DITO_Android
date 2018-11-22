package com.team_yapp.kiyon.yappproject.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team_yapp.kiyon.yappproject.R;
import com.team_yapp.kiyon.yappproject.adapter.RoomStatisticsRVAdapter;
import com.team_yapp.kiyon.yappproject.common.RetrofitServerClient;
import com.team_yapp.kiyon.yappproject.common.StatusBarColorChange;
import com.team_yapp.kiyon.yappproject.model.Room.RoomStatisticsResponseResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomStatisticsActivity extends AppCompatActivity {
    private static final String TAG = "방 통계";

    public static final String ROOM_SUB_NAME = "ROOM_SUB_NAME";
    public static final String ROOM_CODE = "ROOM_CODE";

    private String tmCode;

    private RoomStatisticsRVAdapter roomStatisticsRVAdapter;

    public static Intent newIntent(Context context, String tmcode, String subName) {

        Intent intent = new Intent(context, RoomStatisticsActivity.class);
        intent.putExtra(ROOM_CODE, tmcode);
        intent.putExtra(ROOM_SUB_NAME, subName);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_statistics);

        initialize();
        loadRoomStatisticsInfo(tmCode);

    }

    private void initialize() {
        // 상태바 셋팅
        StatusBarColorChange.setStatusBarColor(RoomStatisticsActivity.this, getResources().getColor(R.color.yellow));

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_arrow_back);

        // 인텐트값 가져오기
        Intent intent = getIntent();
        tmCode = intent.getStringExtra(ROOM_CODE);
        String subName = intent.getStringExtra(ROOM_SUB_NAME);

        // 툴바 제목 설정
        TextView toolbar_title = findViewById(R.id.title_toolbar);
        toolbar_title.setText(subName);

        // recyclerview 셋팅
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomStatisticsRVAdapter = new RoomStatisticsRVAdapter(RoomStatisticsActivity.this, (ViewGroup) findViewById(R.id.container));
        recyclerView.setAdapter(roomStatisticsRVAdapter);
    }

    private void loadRoomStatisticsInfo(String tmCode) {

        Call<ArrayList<RoomStatisticsResponseResult>> call = RetrofitServerClient.getInstance().getService().RoomStatistics(tmCode);
        call.enqueue(new Callback<ArrayList<RoomStatisticsResponseResult>>() {
            @Override
            public void onResponse(Call<ArrayList<RoomStatisticsResponseResult>> call, Response<ArrayList<RoomStatisticsResponseResult>> response) {
                if (response.isSuccessful()) {
                    ArrayList<RoomStatisticsResponseResult> roomStatisticsResponseResults = response.body();
                    if (roomStatisticsResponseResults != null) {
                        roomStatisticsRVAdapter.setData(roomStatisticsResponseResults);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RoomStatisticsResponseResult>> call, Throwable t) {
                Log.d(TAG , t.getMessage());
            }
        });

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
