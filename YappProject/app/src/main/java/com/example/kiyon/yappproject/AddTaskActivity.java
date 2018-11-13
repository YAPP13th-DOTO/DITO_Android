package com.example.kiyon.yappproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.model.AddTaskResponseResult;
import com.example.kiyon.yappproject.model.RoomList.UserResponseResult;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTaskActivity extends AppCompatActivity {

    public static final String USER_DATA = "USER_DATA";

    private EditText taskName_edit, taskSub_edit;
    private TextView memberCount, dateCount;
    private Button memberBtn, dateBtn,createBtn;
    private MaterialCalendarView materialCalender;
    private InputMethodManager inputMethodManager;
    private ArrayList<UserResponseResult> userResponseResults = new ArrayList<>();
    private ArrayList<UserResponseResult> add_member_list = new ArrayList<>();
    private String shot_Day = "";
    ArrayList<String> users = new ArrayList<>();

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
                int year = date.getYear();
                int month = date.getMonth()+1;
                int day = date.getDay();

                shot_Day = year + "년 " +  month + "월 " + day + "일";

                dateCount.setText(shot_Day);

                materialCalender.setVisibility(View.INVISIBLE);
                memberBtn.setVisibility(View.VISIBLE);
                dateBtn.setVisibility(View.VISIBLE);
            }
        });
        createBtn.setEnabled(false);

        dateCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(memberCount.length() == 0 || charSequence.length() == 0) {
                    createBtn.setEnabled(false);
                    createBtn.setBackgroundColor(Color.parseColor("#bfbfbf"));
                }else if(memberCount.length() == 0 && charSequence.length() == 0) {
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
                //SharedPreferences sharedPreferences = getSharedPreferences("DITO",MODE_PRIVATE);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddTaskActivity.this);
                String tmcode =  sharedPreferences.getString("tmcode",null);

                String asname = taskName_edit.getText().toString();
                String ascontent = taskSub_edit.getText().toString();
                String asdl = shot_Day;

                Log.e("TAG","users = " + users);
                createTask(tmcode,asname,ascontent,asdl,users);

                break;
            case R.id.taskDateBtn:
                materialCalender.setVisibility(View.VISIBLE);
                memberBtn.setVisibility(View.INVISIBLE);
                dateBtn.setVisibility(View.INVISIBLE);
                break;
            case R.id.taskMemberBtn:
                Intent intent = TaskMemberActivity.newIntent(AddTaskActivity.this,userResponseResults);
                startActivityForResult(intent,3000);
                break;
            case R.id.relativeLayout:
                inputMethodManager.hideSoftInputFromWindow(taskSub_edit.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(taskName_edit.getWindowToken(), 0);
                break;
            case R.id.backBtn:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3000:
                    memberCount.setText(data.getStringExtra("result"));
                    users = data.getStringArrayListExtra("users");
                    Log.e("TAG","users = " + users);
                    break;
            }
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

    private void createTask(String tmcode, String asname, String ascontent, String asdl, ArrayList<String> users) {

        Call<AddTaskResponseResult> call = RetrofitServerClient.getInstance().getService().addTaskResponseResult(tmcode,asname,ascontent,asdl,users);
        Log.e("TAG",String.valueOf(call.request().url()));
        call.enqueue(new Callback<AddTaskResponseResult>() {
            @Override
            public void onResponse(Call<AddTaskResponseResult> call, Response<AddTaskResponseResult> response) {
                if(response.isSuccessful()) {
                    Log.e("TAG_R","success");
                    if(response.isSuccessful()) {
                        AddTaskResponseResult addTaskResponseResult = response.body();
                        if(addTaskResponseResult != null) {
                            Log.e("TAG","addTaskResponseResult success");
                        }
                    }
                }else {
                    Log.e("TAG_R", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<AddTaskResponseResult> call, Throwable t) {
                Log.e("TAG_F",t.getMessage());
            }
        });
    }

}
