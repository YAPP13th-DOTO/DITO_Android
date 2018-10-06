package com.example.kiyon.yappproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class linkCreateActivity extends AppCompatActivity {


    Button linkCopyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_create);

        linkCopyBtn=(Button)findViewById(R.id.linkCopyBtn);
        linkCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "링크를 클립보드에 복사했습니다.", Toast.LENGTH_LONG).show();

            }
        });

        //링크 복사 버튼 누를시, 링크 받아옴


    }
}
