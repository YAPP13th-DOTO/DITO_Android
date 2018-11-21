package com.example.kiyon.yappproject.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.common.StatusBarColorChange;
import com.example.kiyon.yappproject.common.UserInfoReturn;
import com.example.kiyon.yappproject.model.Room.RoomCreateResponseResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomCreateActivity extends AppCompatActivity {

    private Button create_btn;
    private EditText roomName_edit, subjectName_edit;

    public static Intent newIntent(Context context) {

        return new Intent(context, RoomCreateActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_create);

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_arrow_back);

        // 툴바 제목 셋팅
        TextView textView = findViewById(R.id.title_toolbar);
        textView.setText("새 팀플 만들기");

        // 상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(RoomCreateActivity.this, getResources().getColor(R.color.yellow));

        roomName_edit = findViewById(R.id.roomName_edit);
        subjectName_edit = findViewById(R.id.subjectName_edit);

        create_btn = findViewById(R.id.create_btn);
        create_btn.setEnabled(false);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData(roomName_edit.getText().toString(), subjectName_edit.getText().toString());
            }
        });

        subjectName_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (roomName_edit.length() == 0 && charSequence.length() == 0 ) {
                    create_btn.setEnabled(false);
                    create_btn.setBackgroundColor(Color.parseColor("#bfbfbf"));
                } else if (roomName_edit.length() == 0 || charSequence.length() == 0 ) {
                    create_btn.setEnabled(false);
                    create_btn.setBackgroundColor(Color.parseColor("#bfbfbf"));
                } else {
                    create_btn.setEnabled(true);
                    create_btn.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void loadData(final String roomName, final String subjectName) {

        String userID = UserInfoReturn.getInstance().getUserId(RoomCreateActivity.this);

        Call<RoomCreateResponseResult> call = RetrofitServerClient.getInstance().getService().CreateRoomResponseResult(userID, roomName, subjectName);
        call.enqueue(new Callback<RoomCreateResponseResult>() {
            @Override
            public void onResponse(Call<RoomCreateResponseResult> call, Response<RoomCreateResponseResult> response) {
                if (response.isSuccessful()) {
                    RoomCreateResponseResult roomCreateResponseResult = response.body();
                    if (roomCreateResponseResult != null) {
                        Intent intent = RoomSuccessActivity.newIntent(RoomCreateActivity.this, roomName, subjectName, roomCreateResponseResult.code);
                        startActivity(intent);
                        finish();

                    } else {
                        Log.d("RoomCreateActivity", "오류");

                    }
                }
            }

            @Override
            public void onFailure(Call<RoomCreateResponseResult> call, Throwable t) {
                Log.d("RoomCreateActivity", t.getMessage());

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
