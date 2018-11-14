package com.example.kiyon.yappproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.model.Task.TaskAttendUsersItem;

import java.util.ArrayList;

public class TaskAttendUsersRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    public ArrayList<TaskAttendUsersItem> taskAttendUsersItems;

    public TaskAttendUsersRVAdapter(Context context) {
        mContext = context;
        taskAttendUsersItems = new ArrayList<>();
    }

    public void setData(ArrayList<TaskAttendUsersItem> lists) {
        taskAttendUsersItems.clear();
        taskAttendUsersItems.addAll(lists);
        notifyDataSetChanged();
    }

    private class  TaskAttendUsersVH extends RecyclerView.ViewHolder {
        private ImageView userImage;
        private TextView userName;
        private TextView lateStatus;
        private TextView submitStatus;

        public TaskAttendUsersVH(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.profile_iv);
            userName = itemView.findViewById(R.id.userName_tv);
            lateStatus = itemView.findViewById(R.id.late_status_tv);
            submitStatus = itemView.findViewById(R.id.submit_status_tv);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskAttendUsersVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_task_attenduser, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskAttendUsersVH taskAttendUsersVH = (TaskAttendUsersVH) holder;

        if (taskAttendUsersItems.get(position).user_pic.equals("undefined")) {
            Glide.with(mContext).load(R.drawable.test_user).into(taskAttendUsersVH.userImage);
        } else {
            Glide.with(mContext).load(taskAttendUsersItems.get(position).user_pic).into(taskAttendUsersVH.userImage);
        }

        taskAttendUsersVH.userName.setText(taskAttendUsersItems.get(position).user_name);
    }

    @Override
    public int getItemCount() {
        return taskAttendUsersItems.size();
    }
}
