package com.example.kiyon.yappproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kiyon.yappproject.common.CustomTypefaceSpan;
import com.example.kiyon.yappproject.common.StatusBarColorChange;

public class RoomSuccessActivity extends AppCompatActivity {

    public static final String ROOM_NAME = "ROOMNAME";
    public static final String SUBJECT_NAME = "SUBJECT_NAME";
    public static final String ROOM_CODE = "ROOM_CODE";

    private TextView title_tv;
    private Dialog dialog;

    public static Intent newIntent(Context context, String roomName, String subjectName, String roomCode) {
        Intent intent = new Intent(context, RoomSuccessActivity.class);
        intent.putExtra(ROOM_NAME, roomName);
        intent.putExtra(SUBJECT_NAME, subjectName);
        intent.putExtra(ROOM_CODE, roomCode);
        return intent;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_success);

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
        StatusBarColorChange.setStatusBarColor(RoomSuccessActivity.this, getResources().getColor(R.color.yellow));

        // intent 가져오기
        final Intent intent = getIntent();
        String roomName = intent.getStringExtra(ROOM_NAME);
        String subjectName = "'" + intent.getStringExtra(SUBJECT_NAME) + "'";
        final String roomCode = intent.getStringExtra(ROOM_CODE);

        title_tv = findViewById(R.id.title_tv);

        // 특정 텍스트 폰트 적용
        Typeface nanumBoldFont = Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf");

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(roomName + " 과목");
        spannableStringBuilder.insert(roomName.length() + 3,"\n");

        SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder(subjectName + " 방이 생성되었습니다!");
        spannableStringBuilder1.insert(subjectName.length() + 12, "\n");

        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("팀원들에게 링크를 공유해주세요.");

        // 폰트 적용
        spannableStringBuilder.setSpan (new CustomTypefaceSpan("", nanumBoldFont), 0, roomName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder1.setSpan (new CustomTypefaceSpan("", nanumBoldFont), 0, subjectName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        // 가운데 정렬
        spannableStringBuilder.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder1.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, spannableStringBuilder1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        //SpannableStringBuilder 합치기
        CharSequence charSequence = TextUtils.concat(spannableStringBuilder, spannableStringBuilder1, spannableStringBuilder2);
        title_tv.setText(charSequence);


        // 초대링크 버튼
        findViewById(R.id.invite_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클립보드에 방코드 저장
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("방코드",roomCode);
                Log.d("roomValue", "클립보드:"+ roomCode);
                clipboardManager.setPrimaryClip(clipData);
                //방코드 저장
                //SharedPreferences sharedPreferences = getSharedPreferences("DITO", MODE_PRIVATE);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("tmcode",roomCode);
                editor.commit();

                setDialogView();
            }
        });

        dialog = new Dialog(this);


    }

    void setDialogView() {

        dialog.setContentView(R.layout.dialog_clipboard_copy);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);

        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
