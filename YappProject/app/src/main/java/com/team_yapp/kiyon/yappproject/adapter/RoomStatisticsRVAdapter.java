package com.team_yapp.kiyon.yappproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.team_yapp.kiyon.yappproject.R;
import com.team_yapp.kiyon.yappproject.model.Room.RoomStatisticsResponseResult;
import com.team_yapp.kiyon.yappproject.model.Task.TaskStatisticsResponseResult;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class RoomStatisticsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ViewGroup mRootView;
    private ArrayList<RoomStatisticsResponseResult> roomStatisticsResponseResults;
    private ArrayList<TaskStatisticsResponseResult> taskStatisticsResponseResults;
    ArrayList<TaskStatisticsResponseResult> completeTaskList;
    ArrayList<TaskStatisticsResponseResult> lateTaskList;
    ArrayList<TaskStatisticsResponseResult> uncompleteTaskList;

    public RoomStatisticsRVAdapter(Context context, ViewGroup rootView) {
        mContext = context;
        mRootView = rootView;
        roomStatisticsResponseResults = new ArrayList<>();
        taskStatisticsResponseResults = new ArrayList<>();
        completeTaskList = new ArrayList<>();
        lateTaskList = new ArrayList<>();
        uncompleteTaskList = new ArrayList<>();
    }

    public void setData(ArrayList<RoomStatisticsResponseResult> lists) {
        roomStatisticsResponseResults.clear();
        roomStatisticsResponseResults.addAll(lists);
        notifyDataSetChanged();
    }

    private class RoomStatisticsVH extends RecyclerView.ViewHolder {
        private CircleImageView user_Image;
        private TextView user_Name;
        private TextView percent;
        private TextView taskCount;
        private ImageView moreInfo;
        private RelativeLayout moreclick_layout;
        private RelativeLayout moreInfo_layout;
        private PieChartView chartView;
        private RecyclerView complete_recyclerview;
        private RecyclerView late_recyclerview;
        private RecyclerView uncomplete_recyclerview;

        public RoomStatisticsVH(View itemView) {
            super(itemView);
            user_Image = itemView.findViewById(R.id.profile_image);
            user_Name = itemView.findViewById(R.id.userName);
            percent = itemView.findViewById(R.id.percent_txt);
            taskCount = itemView.findViewById(R.id.taskCount);
            moreclick_layout = itemView.findViewById(R.id.relativeLayout1);
            moreInfo = itemView.findViewById(R.id.moreInfo);
            moreInfo_layout = itemView.findViewById(R.id.moreinfo_layout);
            chartView = itemView.findViewById(R.id.chart);
            complete_recyclerview = itemView.findViewById(R.id.completeRecyclerview);
            late_recyclerview = itemView.findViewById(R.id.lateRecyclerview);
            uncomplete_recyclerview = itemView.findViewById(R.id.uncompleteRecyclerview);

            moreclick_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TransitionManager.beginDelayedTransition(mRootView, new ChangeBounds());
                    changeViewState();
                }
            });
        }

        private void changeViewState() {
            if (View.GONE == moreInfo_layout.getVisibility()) {
                moreInfo.setImageResource(R.drawable.icn_arrow_up);
                moreInfo_layout.setVisibility(View.VISIBLE);
            } else {
                moreInfo.setImageResource(R.drawable.icn_arrow_down);
                moreInfo_layout.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomStatisticsVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_room_statistics, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RoomStatisticsVH roomStatisticsVH = (RoomStatisticsVH) holder;

        // 유저 이미지 설정
        if(roomStatisticsResponseResults.get(position).user_pic.equals("undefined")) {
            Glide.with(mContext).load(R.drawable.temp_user_image).into(roomStatisticsVH.user_Image);
        }else {
            Glide.with(mContext).load(roomStatisticsResponseResults.get(position).user_pic).into(roomStatisticsVH.user_Image);
        }

        // 유저이름 설정
        roomStatisticsVH.user_Name.setText(roomStatisticsResponseResults.get(position).user_name);

        // 달성도 설정
        if (String.valueOf(roomStatisticsResponseResults.get(position).percent.get(0).percent).equals("null")) { // 수행한 과제가 없을 경우
            roomStatisticsVH.percent.setText(String.valueOf("0"));
        } else {
            roomStatisticsVH.percent.setText(roomStatisticsResponseResults.get(position).percent.get(0).percent);

        }

        // 참여한 과제 갯수 설정
        roomStatisticsVH.taskCount.setText(String.valueOf(roomStatisticsResponseResults.get(position).as_name.size() + "회"));

        // recyclerview 셋팅
        roomStatisticsVH.complete_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        TaskStatisticsRVAdapter completeRVAdapter = new TaskStatisticsRVAdapter(mContext);
        roomStatisticsVH.complete_recyclerview.setAdapter(completeRVAdapter);

        roomStatisticsVH.late_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        TaskStatisticsRVAdapter lateRVAdapter = new TaskStatisticsRVAdapter(mContext);
        roomStatisticsVH.late_recyclerview.setAdapter(lateRVAdapter);

        roomStatisticsVH.uncomplete_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        TaskStatisticsRVAdapter uncompleteRVAdapter = new TaskStatisticsRVAdapter(mContext);
        roomStatisticsVH.uncomplete_recyclerview.setAdapter(uncompleteRVAdapter);

        // 단일 유저가 참여한 과제 목록
        taskStatisticsResponseResults.addAll(roomStatisticsResponseResults.get(position).as_name);

        // 유저가 참여한 과제가 지각,정시완료,미제출 어떤 종류인지 확인하기 위한 반복문
        for (int i = 0; i < taskStatisticsResponseResults.size(); i++) {
            if (taskStatisticsResponseResults.get(i).accept == 1 && taskStatisticsResponseResults.get(i).late == 1) { // 지각 과제
                lateTaskList.add(taskStatisticsResponseResults.get(i));
            } else if (taskStatisticsResponseResults.get(i).accept == 1 && taskStatisticsResponseResults.get(i).late == 0) { // 정시완료 과제
                completeTaskList.add(taskStatisticsResponseResults.get(i));
            } else { // 미제출 과제
                uncompleteTaskList.add(taskStatisticsResponseResults.get(i));
            }

            if (i == taskStatisticsResponseResults.size() -1) { // 반복문이 끝날경우 어댑터에 데이터를 넘겨줌
                lateRVAdapter.setData(lateTaskList);
                completeRVAdapter.setData(completeTaskList);
                uncompleteRVAdapter.setData(uncompleteTaskList);

                // 차트 그리기
                List<SliceValue> pieData = new ArrayList<>();
                pieData.add(new SliceValue(completeTaskList.size(), Color.parseColor("#c3c3c3")));   //정시완료
                pieData.add(new SliceValue(lateTaskList.size(), Color.parseColor("#999999")));   //지각
                pieData.add(new SliceValue(uncompleteTaskList.size(), Color.parseColor("#666666")));   //미완료
                PieChartData pieChartData = new PieChartData(pieData);
                roomStatisticsVH.chartView.setPieChartData(pieChartData);

                // arraylist에 새로운 정보를 담기위해 clear
                lateTaskList.clear();
                completeTaskList.clear();
                uncompleteTaskList.clear();
                taskStatisticsResponseResults.clear();
            }
        }



    }

    @Override
    public int getItemCount() {
        return roomStatisticsResponseResults.size();
    }
}
