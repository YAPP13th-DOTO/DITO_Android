package com.example.kiyon.yappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class orgCreateActivity extends AppCompatActivity {

    Button orgCreateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_create);

        orgCreateBtn=(Button)findViewById(R.id.orgCreateBtn);
        orgCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(orgCreateActivity.this,linkCreateActivity.class);
                startActivity(intent);
            }
        });
        //만들기 버튼 누를시, 링크복사 화면으로 넘어감


    }
}
