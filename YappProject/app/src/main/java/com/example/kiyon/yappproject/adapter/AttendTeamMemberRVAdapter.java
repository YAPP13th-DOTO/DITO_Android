package com.example.kiyon.yappproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.model.RoomList.UserResponseResult;
import com.kakao.usermgmt.response.model.User;

import java.util.ArrayList;

public class AttendTeamMemberRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<UserResponseResult> userResponseResults;
    private AttendTeamMemberRVAdapter.ItemClickListener itemClickListener;

    public AttendTeamMemberRVAdapter(ArrayList<UserResponseResult> lists,Context context) {
        this.context = context;
        this.userResponseResults = lists;
        //userResponseResults = new ArrayList<>();
    }

    public class AttendTeamMemberRV extends RecyclerView.ViewHolder {
        private ImageView profile_img;
        private TextView user_name;

        public AttendTeamMemberRV(View itemView) {
            super(itemView);
            profile_img = itemView.findViewById(R.id.image);
            user_name = itemView.findViewById(R.id.user_name);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_attend_member,parent,false);
        return new AttendTeamMemberRV(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AttendTeamMemberRV attendTeamMemberRV = (AttendTeamMemberRV) holder;

        if(userResponseResults.get(position).user_pic.equals("undefined")) {
            Glide.with(context).load(R.drawable.test_user).into(attendTeamMemberRV.profile_img);
        }else {
            Glide.with(context).load(userResponseResults.get(position).user_pic).into(attendTeamMemberRV.profile_img);
        }
        attendTeamMemberRV.user_name.setText(userResponseResults.get(position).user_name);
    }

    @Override
    public int getItemCount() {
        return userResponseResults.size();
    }

    public void setClickListener(AttendTeamMemberRVAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void onItemClick(int pos, boolean check, UserResponseResult person);

    }
}
