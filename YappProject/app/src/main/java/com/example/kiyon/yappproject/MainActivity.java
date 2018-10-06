package com.example.kiyon.yappproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.kiyon.yappproject.common.StatusBarColorChange;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("앱이름");
        setSupportActionBar(toolbar);

        // 상태바 셋팅
        StatusBarColorChange.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.white));
    }
}
