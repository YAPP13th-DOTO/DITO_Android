package com.example.kiyon.yappproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.common.UserInfoReturn;
import com.example.kiyon.yappproject.model.Room.RoomAttendUsersItem;
import com.example.kiyon.yappproject.model.Task.TaskInfoItem;

import java.util.ArrayList;

public class ProjectCompleteRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<RoomAttendUsersItem> list;
    String tm_code;

    public ProjectCompleteRVAdapter(Context context, ArrayList<RoomAttendUsersItem> list ,String tm_code) {
        this.context = context;
        this.list = list;
        this.tm_code = tm_code;
    }

    public class ProjectCompleteRV extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayout2;     //통계차트있는 뷰
        private CheckBox moreInfo;
        private TextView user_name;
        private RecyclerView onTime,late,uncomplete;    //정시제출,지각,미제출 시 과제이름
        private ImageView profile_img;

        public ProjectCompleteRV(View itemView) {
            super(itemView);

            relativeLayout2 = itemView.findViewById(R.id.relativeLayout2);
            moreInfo = itemView.findViewById(R.id.moreInfo);
            user_name = itemView.findViewById(R.id.userName);
            onTime = itemView.findViewById(R.id.onTimeRecyclerview);
            late = itemView.findViewById(R.id.lateRecyclerview);
            uncomplete = itemView.findViewById(R.id.uncompleteRecyclerview);
            profile_img = itemView.findViewById(R.id.profile_image);
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

        //프로필사진 유무
        if(list.get(position).user_pic.equals("undefined")) {
            Glide.with(context).load(R.drawable.test_user).into(projectCompleteRV.profile_img);
        }else {
            Glide.with(context).load(list.get(position).user_pic).into(projectCompleteRV.profile_img);
        }

        //클릭유무
        if(projectCompleteRV.moreInfo.isChecked()) {
            Glide.with(context).load(R.drawable.icn_arrow_up);
        } else {
            Glide.with(context).load(R.drawable.icon_arrow_down);
        }

        projectCompleteRV.user_name.setText(list.get(position).user_name + "님의 과제 성실도");

        /*
        ***통계차트 그리기
        PieChartView pieChartView = findViewById(R.id.chart);

        List<SliceValue> pieData = new ArrayList<>();

        pieData.add(new SliceValue(30, Color.parseColor("#c3c3c3")));   //정시완료
        pieData.add(new SliceValue(10, Color.parseColor("#999999")));   //지각
        pieData.add(new SliceValue(10, Color.parseColor("#666666")));   //미완료

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasCenterCircle(true);
        pieChartView.setPieChartData(pieChartData);
         */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
