package com.example.kiyon.yappproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.model.Etc.BasicResponseResult;
import com.example.kiyon.yappproject.model.Room.RoomAttendUsersItem;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskCreateActivity extends AppCompatActivity {

    public static final String USER_DATA = "USER_DATA";

    private EditText taskName_edit, taskSub_edit;
    private TextView memberCount, dateCount;
    private Button memberBtn, dateBtn,createBtn;
    private MaterialCalendarView materialCalender;
    private InputMethodManager inputMethodManager;
    private ArrayList<RoomAttendUsersItem> roomAttendUsersItems = new ArrayList<>();
    private String shot_Day = "";
    private ArrayList<RoomAttendUsersItem> attendUserLists = new ArrayList<>();
    private ArrayList<String> attendUserIdLists = new ArrayList<>();

    private String taskDeadline;

    public static Intent newIntent(Context context, ArrayList<RoomAttendUsersItem> list) {
        Intent intent = new Intent(context, TaskCreateActivity.class);
        intent.putExtra(USER_DATA, list);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

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
        roomAttendUsersItems = (ArrayList<RoomAttendUsersItem>) intent.getSerializableExtra(USER_DATA);

        // 오늘 날짜 가져오기
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //달력 설정
        materialCalender = findViewById(R.id.materialCalender);

        materialCalender.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(currentYear, currentMonth, currentDay)) // 오늘기준 과거 날짜를 선택하기 못한 선택
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

                shot_Day = year + "년 " +  month + "월 " + day + "일"; // 화면에 보여줄 마감시간
                String formatString = year + "년 " +  month + "월 " + day + "일 " + "23시 59분 59초"; // 서버에 전송할 마감시간

                try { // 서버에서 원하는 형식으로 변형
                    Date date1 = new SimpleDateFormat("yyyy년 M월 d일 HH시 mm분 ss초").parse(formatString);
                    taskDeadline = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dateCount.setText(shot_Day);

                materialCalender.setVisibility(View.INVISIBLE);
                memberBtn.setVisibility(View.VISIBLE);
                dateBtn.setVisibility(View.VISIBLE);
            }
        });
        createBtn.setEnabled(false);

        //taskName_edittaskSub_edit
        dateCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(memberCount.length() == 0 || charSequence.length() == 0 || taskSub_edit.getText() == null || taskName_edit.getText() == null) {
                    createBtn.setEnabled(false);
                    createBtn.setBackgroundColor(Color.parseColor("#bfbfbf"));
                } else {
                    createBtn.setEnabled(true);
                    createBtn.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        memberCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(dateCount.length() == 0 || charSequence.length() == 0 || taskSub_edit.getText() == null || taskName_edit.getText() == null) {
                    createBtn.setEnabled(false);
                    createBtn.setBackgroundColor(Color.parseColor("#bfbfbf"));
                } else {
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
            case R.id.taskCreateBtn: // 과제 만들기 버튼
                if (taskName_edit.getText() == null) {
                    Toasty.warning(TaskCreateActivity.this, "과제명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (taskSub_edit.getText() == null) {
                    Toasty.warning(TaskCreateActivity.this, "과제 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (memberCount.getText().equals("0명") || memberCount.length() == 0) {
                    Toasty.warning(TaskCreateActivity.this, "과제 수행자를 지정해주세요.", Toast.LENGTH_SHORT).show();
                } else if (dateCount.length() == 0) {
                    Toasty.warning(TaskCreateActivity.this, "과제 기한을 설정해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String tmcode =  attendUserLists.get(0).tm_code;
                    String taskName = taskName_edit.getText().toString(); // 과제명
                    String taskContent = taskSub_edit.getText().toString(); // 과제 내용

                    createTask(tmcode, taskName, taskContent, taskDeadline, attendUserIdLists);
                }

                break;
            case R.id.taskDateLayout: // 기한 설정 하기
                materialCalender.setVisibility(View.VISIBLE);
                memberBtn.setVisibility(View.INVISIBLE);
                dateBtn.setVisibility(View.INVISIBLE);
                break;
            case R.id.taskMemberLayout: // 과제 멤버 추가하기
                Intent intent = TaskMemberActivity.newIntent(TaskCreateActivity.this, roomAttendUsersItems);
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
                    attendUserLists = (ArrayList<RoomAttendUsersItem>) data.getSerializableExtra("users");
                    attendUserIdLists.clear();
                    for (int i = 0; i < attendUserLists.size(); i++) {
                        attendUserIdLists.add(attendUserLists.get(i).kakao_id);
                    }
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

    private void createTask(String tmcode, String taskName, String taskContent, String taskDeadline, ArrayList<String> attendUserIdLists) {

        Call<BasicResponseResult> call = RetrofitServerClient.getInstance().getService().AddTaskResponseResult(tmcode, taskName, taskContent, taskDeadline, attendUserIdLists);
        call.enqueue(new Callback<BasicResponseResult>() {
            @Override
            public void onResponse(Call<BasicResponseResult> call, Response<BasicResponseResult> response) {
                if(response.isSuccessful()) {
                    BasicResponseResult basicResponseResult = response.body();
                    if (basicResponseResult != null) {
                        if (basicResponseResult.answer.equals("access")) { // 과제 추가하기 성공
                            Intent intent = new Intent(TaskCreateActivity.this, RoomDetailActivity.class);
                            setResult(RESULT_OK,intent);
                            finish();
                        } else {
                            // 실패
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseResult> call, Throwable t) {
            }
        });
    }

}
