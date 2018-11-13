package com.example.kiyon.yappproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.model.Task.TaskInfoItem;

import java.util.ArrayList;

public class TaskListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TaskInfoItem> taskInfoItems;

    public TaskListRVAdapter(Context context) {
        mContext = context;
        taskInfoItems = new ArrayList<>();
    }

    public void setData(ArrayList<TaskInfoItem> lists) {
        taskInfoItems.clear();
        taskInfoItems.addAll(lists);
        notifyDataSetChanged();
    }

    private class TaskListVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView task_title;
        private ImageView arrow_iv;
        private AppCompatCheckBox taskSubmit;
        private TextView taskDeadLine;

        public TaskListVH(View itemView) {
            super(itemView);
            task_title = itemView.findViewById(R.id.subjectName_tv);
            arrow_iv = itemView.findViewById(R.id.arrow_iv);
            taskSubmit = itemView.findViewById(R.id.check_iv);
            taskDeadLine = itemView.findViewById(R.id.time_tv);

            arrow_iv.setOnClickListener(this);
            taskSubmit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.check_iv :
                    if (taskSubmit.isChecked()) {
                        taskSubmit.setButtonDrawable(R.drawable.uncheck_1);
                    } else {
                        taskSubmit.setButtonDrawable(R.drawable.check_1);
                    }
                    break;
                case R.id.arrow_iv : // 과제 참여인원 보기
                    break;
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
    }

    @Override
    public int getItemCount() {
        return taskInfoItems.size();
    }
}
