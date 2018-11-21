package com.example.kiyon.yappproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.model.Task.TaskStatisticsResponseResult;

import java.util.ArrayList;

public class TaskStatisticsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TaskStatisticsResponseResult> taskStatisticsResponseResults;

    public TaskStatisticsRVAdapter(Context context) {
        mContext = context;
        taskStatisticsResponseResults = new ArrayList<>();
    }

    public void setData(ArrayList<TaskStatisticsResponseResult> lists) {
        taskStatisticsResponseResults.clear();
        taskStatisticsResponseResults.addAll(lists);
        notifyDataSetChanged();
    }

    private class TaskStatisticsVH extends RecyclerView.ViewHolder {

        private TextView task_name;

        public TaskStatisticsVH(View itemView) {
            super(itemView);
            task_name = itemView.findViewById(R.id.task_name);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskStatisticsVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_task_statistics, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskStatisticsVH taskStatisticsVH = (TaskStatisticsVH) holder;

        taskStatisticsVH.task_name.setText(taskStatisticsResponseResults.get(position).as_name);
    }

    @Override
    public int getItemCount() {
        return taskStatisticsResponseResults.size();
    }
}
