package com.example.kiyon.yappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.view.RoomDetailActivity;
import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.model.Room.RoomListResponseResult;
import com.example.kiyon.yappproject.model.Room.RoomAttendUsersItem;
import com.example.kiyon.yappproject.view.RoomStatisticsActivity;

import java.util.ArrayList;

public class RoomListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<RoomListResponseResult> roomListResponseResults;
    private static final int EXTRA_PEOPLE = 3;

    public RoomListRVAdapter(Context context) {
        mContext = context;
        roomListResponseResults = new ArrayList<>();
    }

    public void setRoomData(ArrayList<RoomListResponseResult> lists) {
        roomListResponseResults.clear();
        roomListResponseResults.addAll(lists);
        notifyDataSetChanged();
    }


    private class RoomListProgressVH extends RecyclerView.ViewHolder {

        private TextView roomName_tv;
        private TextView subjectName_tv;
        private ImageView roomMaster_iv;
        private TextView additionalUser_tv;
        private ImageView profile_image1;
        private ImageView profile_image2;
        private ImageView profile_image3;

        @SuppressLint("CutPasteId")
        public RoomListProgressVH(final View itemView) {
            super(itemView);
            roomName_tv = itemView.findViewById(R.id.roomName);
            subjectName_tv = itemView.findViewById(R.id.subjectName);
            roomMaster_iv = itemView.findViewById(R.id.roomMaster);
            additionalUser_tv = itemView.findViewById(R.id.additionalUserCount);
            profile_image1 = itemView.findViewById(com.example.kiyon.yappproject.R.id.profile_image1);
            profile_image2 = itemView.findViewById(R.id.profile_image2);
            profile_image3 = itemView.findViewById(R.id.profile_image3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = RoomDetailActivity.newIntent(mContext, roomListResponseResults.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });

        }
    }

    private class RoomListFinishVH extends RecyclerView.ViewHolder {

        private TextView roomName_tv;
        private TextView subjectName_tv;
        private ImageView roomMaster_iv;

        public RoomListFinishVH(View itemView) {
            super(itemView);
            roomName_tv = itemView.findViewById(R.id.roomName);
            subjectName_tv = itemView.findViewById(R.id.subjectName);
            roomMaster_iv = itemView.findViewById(R.id.roomMaster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = RoomStatisticsActivity.newIntent(mContext, roomListResponseResults.get(getAdapterPosition()).tm_code,
                            roomListResponseResults.get(getAdapterPosition()).sub_name);
                    mContext.startActivity(intent);
                }
            });
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new RoomListProgressVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_room_list, parent, false));
        } else {
            return new RoomListFinishVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_finish_roomlist, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RoomListProgressVH) {
            RoomListProgressVH roomListProgressVH = (RoomListProgressVH) holder;

            roomListProgressVH.roomName_tv.setText(roomListResponseResults.get(position).tm_name);
            roomListProgressVH.subjectName_tv.setText(roomListResponseResults.get(position).sub_name);

            // 방에 참여한 유저들 값
            ArrayList<RoomAttendUsersItem> userLists = roomListResponseResults.get(position).users;
            setUserBindView(roomListProgressVH, userLists);

            if (roomListResponseResults.get(position).iscreater != 1) {
                roomListProgressVH.roomMaster_iv.setVisibility(View.GONE);
            }
        } else {
            RoomListFinishVH roomListFinishVH = (RoomListFinishVH) holder;

            roomListFinishVH.roomName_tv.setText(roomListResponseResults.get(position).tm_name);
            roomListFinishVH.subjectName_tv.setText(roomListResponseResults.get(position).sub_name);

            if (roomListResponseResults.get(position).iscreater != 1) {
                roomListFinishVH.roomMaster_iv.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return roomListResponseResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (roomListResponseResults.get(position).isdone == 0) { // 진행중인 방
            return 0;
        } else { // 종료된 방
            return 1;
        }
    }

    private void setUserBindView(RoomListProgressVH roomListProgressVH, ArrayList<RoomAttendUsersItem> lists) {
        if (lists.size() > EXTRA_PEOPLE) { // 참여 인워수가 3명 초과일 경우

            roomListProgressVH.additionalUser_tv.setVisibility(View.VISIBLE);
            roomListProgressVH.additionalUser_tv.setText("+" + String.valueOf(lists.size() - EXTRA_PEOPLE));

            if (lists.get(0).user_pic.equals("undefined")) {
                Glide.with(mContext).load(R.drawable.temp_user_image).into(roomListProgressVH.profile_image1);
            } else {
                Glide.with(mContext).load(lists.get(0).user_pic).into(roomListProgressVH.profile_image1);
            }

            if (lists.get(1).user_pic.equals("undefined")) {
                Glide.with(mContext).load(R.drawable.temp_user_image).into(roomListProgressVH.profile_image2);
            } else {
                Glide.with(mContext).load(lists.get(1).user_pic).into(roomListProgressVH.profile_image2);
            }

            if (lists.get(2).user_pic.equals("undefined")) {
                Glide.with(mContext).load(R.drawable.temp_user_image).into(roomListProgressVH.profile_image3);
            } else {
                Glide.with(mContext).load(lists.get(2).user_pic).into(roomListProgressVH.profile_image3);
            }

        } else { // 참여 인원수가 3명 이하일 경우
            if (lists.size() == 3) { // 3명인 경우
                roomListProgressVH.additionalUser_tv.setVisibility(View.GONE);
                roomListProgressVH.profile_image1.setVisibility(View.VISIBLE);
                roomListProgressVH.profile_image2.setVisibility(View.VISIBLE);
                roomListProgressVH.profile_image3.setVisibility(View.VISIBLE);

                if (lists.get(0).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.temp_user_image).into(roomListProgressVH.profile_image1);
                } else {
                    Glide.with(mContext).load(lists.get(0).user_pic).into(roomListProgressVH.profile_image1);
                }

                if (lists.get(1).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.temp_user_image).into(roomListProgressVH.profile_image2);
                } else {
                    Glide.with(mContext).load(lists.get(1).user_pic).into(roomListProgressVH.profile_image2);
                }

                if (lists.get(2).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.temp_user_image).into(roomListProgressVH.profile_image3);
                } else {
                    Glide.with(mContext).load(lists.get(2).user_pic).into(roomListProgressVH.profile_image3);
                }
            } else if (lists.size() == 2) { // 2명인 경우
                roomListProgressVH.additionalUser_tv.setVisibility(View.GONE);
                roomListProgressVH.profile_image1.setVisibility(View.GONE);
                roomListProgressVH.profile_image2.setVisibility(View.VISIBLE);
                roomListProgressVH.profile_image3.setVisibility(View.VISIBLE);

                if (lists.get(0).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.temp_user_image).into(roomListProgressVH.profile_image2);
                } else {
                    Glide.with(mContext).load(lists.get(0).user_pic).into(roomListProgressVH.profile_image2);
                }

                if (lists.get(1).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.temp_user_image).into(roomListProgressVH.profile_image3);
                } else {
                    Glide.with(mContext).load(lists.get(1).user_pic).into(roomListProgressVH.profile_image3);
                }

            } else { // 한명인 경우
                roomListProgressVH.additionalUser_tv.setVisibility(View.GONE);
                roomListProgressVH.profile_image1.setVisibility(View.GONE);
                roomListProgressVH.profile_image2.setVisibility(View.GONE);
                roomListProgressVH.profile_image3.setVisibility(View.VISIBLE);

                if (lists.get(0).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.temp_user_image).into(roomListProgressVH.profile_image3);
                } else {
                    Glide.with(mContext).load(lists.get(0).user_pic).into(roomListProgressVH.profile_image3);
                }
            }
        }
    }

}
