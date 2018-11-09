package com.example.kiyon.yappproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiyon.yappproject.model.RoomList.UserResponseResult;
import com.kakao.usermgmt.response.model.User;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    public static final String USER_DATA = "USER_DATA";

    private EditText taskName_edit, taskSub_edit;
    private TextView memberCount, dateCount;
    private Button memberBtn, dateBtn,createBtn;
    private MaterialCalendarView materialCalender;
    private InputMethodManager inputMethodManager;
    private ArrayList<UserResponseResult> userResponseResults = new ArrayList<>();
    private ArrayList<UserResponseResult> add_member_list = new ArrayList<>();

    public static Intent newIntent(Context context, ArrayList<UserResponseResult> list) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        intent.putExtra(USER_DATA, list);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        taskName_edit = findViewById(R.id.taskNameEdit);
        taskSub_edit = findViewById(R.id.taskSubEdit);
        memberBtn = findViewById(R.id.taskMemberBtn);
        dateBtn = findViewById(R.id.taskDateBtn);
        memberCount = findViewById(R.id.memberCount);
        dateCount = findViewById(R.id.dateCount);
        createBtn = findViewById(R.id.taskCreateBtn);

        //인텐트 정보
        Intent intent = getIntent();
        userResponseResults = (ArrayList<UserResponseResult>) intent.getSerializableExtra(USER_DATA);
        add_member_list = (ArrayList<UserResponseResult>) intent.getExtras().get("add_member");
        //Log.e("TAG","add member = " + add_member_list.size());
        if(add_member_list != null) {
            memberCount.setText(add_member_list.size() + "명");
        }

        //달력 설정
        materialCalender = findViewById(R.id.materialCalender);

        materialCalender.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(1900, 1, 1))
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalender.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator());

        materialCalender.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int month = date.getMonth()+1;
                int day = date.getDay();

                String shot_Day =  month + "월 " + day + "일";

                dateCount.setText(shot_Day);

                materialCalender.setVisibility(View.INVISIBLE);
                memberBtn.setVisibility(View.VISIBLE);
                dateBtn.setVisibility(View.VISIBLE);
            }
        });

            taskSub_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(taskName_edit.length() == 0 || charSequence.length() == 0) {
                    createBtn.setEnabled(false);
                    createBtn.setBackgroundColor(Color.parseColor("#bfbfbf"));
                }else if(taskName_edit.length() == 0 && charSequence.length() == 0) {
                    createBtn.setEnabled(false);
                    createBtn.setBackgroundColor(Color.parseColor("#bfbfbf"));
                }else {
                    createBtn.setEnabled(true);
                    createBtn.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onClickCreateTask(View v) {
        switch (v.getId()) {
            case R.id.taskCreateBtn:
                break;
            case R.id.taskDateBtn:
                materialCalender.setVisibility(View.VISIBLE);
                memberBtn.setVisibility(View.INVISIBLE);
                dateBtn.setVisibility(View.INVISIBLE);
                break;
            case R.id.taskMemberBtn:
                Intent intent = TaskMemberActivity.newIntent(AddTaskActivity.this,userResponseResults);
                intent.putExtra("member_list",userResponseResults);
                startActivity(intent);
                break;
            case R.id.relativeLayout:
                inputMethodManager.hideSoftInputFromWindow(taskSub_edit.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(taskName_edit.getWindowToken(), 0);

        }
    }

    //토요일 색 변경
    public class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    //일요일 색 변경
    public class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

}
