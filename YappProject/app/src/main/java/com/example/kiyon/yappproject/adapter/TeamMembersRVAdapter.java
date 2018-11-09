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
import com.example.kiyon.yappproject.model.RoomList.RoomListResponseResult;
import com.example.kiyon.yappproject.model.RoomList.UserResponseResult;

import java.util.ArrayList;

public class TeamMembersRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<UserResponseResult> mLists;

    public TeamMembersRVAdapter(Context context) {
        mContext = context;
        mLists = new ArrayList<>();
    }

    public void setData(ArrayList<UserResponseResult> lists) {
        mLists.clear();
        mLists.addAll(lists);
        notifyDataSetChanged();
    }

    private class TeamMembersVH extends RecyclerView.ViewHolder {

        private ImageView profileImage;
        private TextView userName;

        public TeamMembersVH(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_iv);
            userName = itemView.findViewById(R.id.userName_tv);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_room_members, parent, false);
        return new TeamMembersVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TeamMembersVH teamMembersVH = (TeamMembersVH) holder;

        // 유저 이미지
        if (mLists.get(position).user_pic.equals("undefined")) { // 저장된 이미지가 없을 경우
            Glide.with(mContext).load(R.drawable.test_user).into(teamMembersVH.profileImage);
        } else {
            Glide.with(mContext).load(mLists.get(position).user_pic).into(teamMembersVH.profileImage);
        }
        // 유저 이름
        teamMembersVH.userName.setText(mLists.get(position).user_name);
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }


}
