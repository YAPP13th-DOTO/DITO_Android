package com.example.kiyon.yappproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.model.Task.TaskCompleteItem;

import java.util.ArrayList;

public class ProjectCompleteRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    ArrayList<TaskCompleteItem> taskCompleteItems;

    public ProjectCompleteRVAdapter(Context context, ArrayList<TaskCompleteItem> list) {
        this.context = context;
        this.taskCompleteItems = list;
    }


    public class ProjectCompleteRV extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView profile_img;
        private TextView memberTaskSincerity;
        private TextView percent_txt;
        private CheckBox moreTask;

        public ProjectCompleteRV(View itemView) {
            super(itemView);
            profile_img = itemView.findViewById(R.id.profile_image);
            memberTaskSincerity = itemView.findViewById(R.id.memberTaskSincerity);
            percent_txt = itemView.findViewById(R.id.percent_txt);
            moreTask = itemView.findViewById(R.id.moreTask);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.relativeLayout:
                    break;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_project_complete,parent,false);
        return new ProjectCompleteRV(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ProjectCompleteRV projectCompleteRV = (ProjectCompleteRV) holder;

        //유저이미지
        if(taskCompleteItems.get(position).user_pic.equals("undefined")) {
            Glide.with(context).load(R.drawable.test_user).into(projectCompleteRV.profile_img);
        } else {
            Glide.with(context).load(R.drawable.test_user).into(projectCompleteRV.profile_img);
        }
        //과제성실도 text
        projectCompleteRV.memberTaskSincerity.setText(taskCompleteItems.get(position).task_member + "님의 과제성실도");
        //퍼센트 text
        projectCompleteRV.percent_txt.setText(taskCompleteItems.get(position).task_percent + "%");

        //체크박스로 더보기 버튼 클릭이벤트
        projectCompleteRV.moreTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(projectCompleteRV.moreTask.isChecked()) {
                    projectCompleteRV.moreTask.setButtonDrawable(R.drawable.icon_arrow_down);
                } else {
                    projectCompleteRV.moreTask.setButtonDrawable(R.drawable.icn_arrow_up);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskCompleteItems.size();
    }
}
