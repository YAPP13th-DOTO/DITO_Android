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
import com.example.kiyon.yappproject.model.Room.RoomAttendUsersItem;

import java.util.ArrayList;

public class AttendTeamMemberRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<RoomAttendUsersItem> roomAttendUsersItems;

    public AttendTeamMemberRVAdapter(ArrayList<RoomAttendUsersItem> lists, Context context) {
        this.context = context;
        this.roomAttendUsersItems = lists;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final AttendTeamMemberRV attendTeamMemberRV = (AttendTeamMemberRV) holder;

        if(roomAttendUsersItems.get(position).user_pic.equals("undefined")) {
            Glide.with(context).load(R.drawable.temp_user_image).into(attendTeamMemberRV.profile_img);
        }else {
            Glide.with(context).load(roomAttendUsersItems.get(position).user_pic).into(attendTeamMemberRV.profile_img);
        }
        attendTeamMemberRV.user_name.setText(roomAttendUsersItems.get(position).user_name);
    }

    @Override
    public int getItemCount() {
        return roomAttendUsersItems.size();
    }
}
