package com.example.kiyon.yappproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.model.Task.TaskInfoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TaskInfoItem> taskInfoItems;
    private Date currentTime;
    private ViewGroup mRootView;


    public TaskListRVAdapter(Context context, ViewGroup rootView) {
        mContext = context;
        taskInfoItems = new ArrayList<>();
        mRootView = rootView;
    }

    public void setData(ArrayList<TaskInfoItem> lists) {
        taskInfoItems.clear();
        taskInfoItems.addAll(lists);
        long now = System.currentTimeMillis();
        currentTime = new Date(now); // 현재 시간 저장

        notifyDataSetChanged();
    }

    private class TaskListVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView task_title;
        private ImageView arrow_iv;
        private AppCompatCheckBox taskSubmit;
        private TextView taskDeadLine;
        private RecyclerView recyclerView;

        public TaskListVH(View itemView) {
            super(itemView);
            task_title = itemView.findViewById(R.id.subjectName_tv);
            arrow_iv = itemView.findViewById(R.id.arrow_iv);
            taskSubmit = itemView.findViewById(R.id.check_iv);
            taskDeadLine = itemView.findViewById(R.id.time_tv);
            recyclerView = itemView.findViewById(R.id.recyclerview);
            arrow_iv.setOnClickListener(this);
            taskSubmit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.check_iv :
                    if (taskSubmit.isChecked()) {
                        taskSubmit.setButtonDrawable(R.drawable.check_1);
                    } else {
                        taskSubmit.setButtonDrawable(R.drawable.uncheck_1);
                    }
                    break;
                case R.id.arrow_iv : // 과제 참여인원 보기
                    TransitionManager.beginDelayedTransition(mRootView, new ChangeBounds());
                    changeViewState();
                    break;
            }
        }

        private void changeViewState() {
            if (View.GONE == recyclerView.getVisibility()) {
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskListVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskListVH taskListVH = (TaskListVH) holder;

        taskListVH.task_title.setText(taskInfoItems.get(position).as_name);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try { // 마감시간 계산
            Date date = simpleDateFormat.parse(taskInfoItems.get(position).as_dl);
            long cal = date.getTime() - currentTime.getTime();
            long calDateDay = cal / (24*60*60*1000);
            if (calDateDay == 0) {
                taskListVH.taskDeadLine.setText(String.valueOf("D-Day"));
            } else if (calDateDay < 0) {
                taskListVH.taskDeadLine.setText("마감");
            } else {
                taskListVH.taskDeadLine.setText(String.valueOf("D-" + calDateDay));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 과제 참여인원을 보여주기위한 자식뷰 설정
        taskListVH.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        TaskAttendUsersRVAdapter taskAttendUsersRVAdapter = new TaskAttendUsersRVAdapter(mContext);
        taskListVH.recyclerView.setAdapter(taskAttendUsersRVAdapter);

        taskAttendUsersRVAdapter.setData(taskInfoItems.get(position).users);
    }

    @Override
    public int getItemCount() {
        return taskInfoItems.size();
    }
}
