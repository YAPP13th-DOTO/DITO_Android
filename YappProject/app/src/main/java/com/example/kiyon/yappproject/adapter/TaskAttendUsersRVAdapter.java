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

        // 지각 유무 판단!
        if (taskAttendUsersItems.get(position).late == 0) {
            taskAttendUsersVH.lateStatus.setText("");
        } else {
            taskAttendUsersVH.lateStatus.setText("지각");
        }

//        & 제출 상태에 대한 경우의 수
//        * 변수명 : req
//        - 승인요청을 유무 값 (1 또는 0)
//        * 변수 명 : accept
//        - 방장 승인 유무 값 (1 또는 0)
//
//        * 경우의 수 (11 10 01 00)
//        - req : 1 accept : 1일 경우
//        유저화면 : (제출완료)
//        방장화면 : (제출완료)
//
//        -req : 1 accept : 0일 경우
//        유저화면 : (승인대기중)
//        방장화면 : o x
//
//        - req : 0 accept : 1 일 경우
//        이런 경우의 수는 없음
//
//        - req : 0 accept : 0일경우
//        유저화면 : 미제출
//        방장화면 : 미제출
        if (taskAttendUsersItems.get(position).req == 0 && taskAttendUsersItems.get(position).accept == 0) {
            taskAttendUsersVH.submitStatus.setText("미제출");
        } else if (taskAttendUsersItems.get(position).req == 1 && taskAttendUsersItems.get(position).accept == 0) {
            // 방장과 유저의 입장차이를 만들어야됨..
        } else if (taskAttendUsersItems.get(position).req == 1 && taskAttendUsersItems.get(position).accept == 1) {
            taskAttendUsersVH.submitStatus.setText("제출완료");
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return taskAttendUsersItems.size();
    }
}
