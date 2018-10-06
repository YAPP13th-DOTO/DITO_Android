package com.example.kiyon.yappproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.common.StatusBarColorChange;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;


public class MainActivity extends AppCompatActivity {

    Animation addBtnOpen, addBtnClose;
    FloatingActionButton roomMakeBtn, roomGoBtn;
    FloatingActionsMenu addBtn;
    RecyclerView recyclerView;
    TextView anotherMember;
    EditText edittext;

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

        addBtn = (FloatingActionsMenu)findViewById(R.id.addBtn);
        addBtnOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.addbtn_open);
        addBtnClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.addbtn_close);

        roomGoBtn = findViewById(R.id.roomGoBtn);
        roomMakeBtn = findViewById(R.id.roomMakeBtn);

        Glide.with(MainActivity.this).load(R.drawable.btn_make).into(roomMakeBtn);
        Glide.with(MainActivity.this).load(R.drawable.btn_participation).into(roomGoBtn);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roomMakeBtn:
                Toast.makeText(getApplicationContext(), "방만들기 버튼 눌림", Toast.LENGTH_SHORT).show();
                addBtn.collapse();
                break;
            case R.id.roomGoBtn:
                Toast.makeText(getApplicationContext(),"참여하기 버튼 눌림", Toast.LENGTH_SHORT).show();
                roomParticipate();
                addBtn.collapse();
                break;
        }
    }

    void roomParticipate()
    {
        edittext = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("코드를 입력하세요.");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),edittext.getText().toString() ,Toast.LENGTH_LONG).show();
                        //코드가 맞으면 방생성
                        //코드가 틀리면 다 지워지고 다시 입력하라고 토스크 띄우기
                        if (edittext.getText().toString() == "방 코드") {

                        } else {
                            Toast.makeText(getApplicationContext(),"코드를 잘못입력하였습니다.",Toast.LENGTH_SHORT).show();
                            edittext.setText("");
                        }
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}
