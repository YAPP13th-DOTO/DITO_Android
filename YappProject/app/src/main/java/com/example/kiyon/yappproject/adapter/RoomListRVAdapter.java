package com.example.kiyon.yappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kiyon.yappproject.RoomDetailActivity;
import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.model.RoomList.RoomListResponseResult;
import com.example.kiyon.yappproject.model.RoomList.UserResponseResult;

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


    public class RoomListVH extends RecyclerView.ViewHolder {

        private TextView roomName_tv;
        private TextView subjectName_tv;
        private ImageView roomMaster_iv;
        private TextView additionalUser_tv;
        private ImageView profile_image1;
        private ImageView profile_image2;
        private ImageView profile_image3;

        @SuppressLint("CutPasteId")
        public RoomListVH(final View itemView) {
            super(itemView);
            roomName_tv = itemView.findViewById(R.id.roomName);
            subjectName_tv = itemView.findViewById(R.id.subjectName);
            roomMaster_iv = itemView.findViewById(R.id.roomMaster);
            additionalUser_tv = itemView.findViewById(R.id.additionalUserCount);
            profile_image1 = itemView.findViewById(R.id.profile_image1);
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomListVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_roomlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RoomListVH roomListVH = (RoomListVH) holder;

        roomListVH.roomName_tv.setText(roomListResponseResults.get(position).tm_name);
        roomListVH.subjectName_tv.setText(roomListResponseResults.get(position).sub_name);

        // 방에 참여한 유저들 값
        ArrayList<UserResponseResult> userLists = roomListResponseResults.get(position).users;
        setUserBindView(roomListVH, userLists);

        if (roomListResponseResults.get(position).iscreater != 1) {
            roomListVH.roomMaster_iv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return roomListResponseResults.size();
    }


    private void setUserBindView(RoomListVH roomListVH, ArrayList<UserResponseResult> lists) {
        if (lists.size() > EXTRA_PEOPLE) { // 참여 인워수가 3명 초과일 경우

            roomListVH.additionalUser_tv.setVisibility(View.VISIBLE);
            roomListVH.additionalUser_tv.setText(lists.size() - EXTRA_PEOPLE);

            if (lists.get(0).user_pic.equals("undefined")) {
                Glide.with(mContext).load(R.drawable.test_user).into(roomListVH.profile_image1);
            } else {
                Glide.with(mContext).load(lists.get(0).user_pic).into(roomListVH.profile_image1);
            }

            if (lists.get(1).user_pic.equals("undefined")) {
                Glide.with(mContext).load(R.drawable.test_user).into(roomListVH.profile_image2);
            } else {
                Glide.with(mContext).load(lists.get(1).user_pic).into(roomListVH.profile_image2);
            }

            if (lists.get(2).user_pic.equals("undefined")) {
                Glide.with(mContext).load(R.drawable.test_user).into(roomListVH.profile_image3);
            } else {
                Glide.with(mContext).load(lists.get(2).user_pic).into(roomListVH.profile_image3);
            }

        } else { // 참여 인원수가 3명 이하일 경우
            if (lists.size() == 3) { // 3명인 경우
                roomListVH.additionalUser_tv.setVisibility(View.GONE);
                roomListVH.profile_image1.setVisibility(View.VISIBLE);
                roomListVH.profile_image2.setVisibility(View.VISIBLE);
                roomListVH.profile_image3.setVisibility(View.VISIBLE);

                if (lists.get(0).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.test_user).into(roomListVH.profile_image1);
                } else {
                    Glide.with(mContext).load(lists.get(0).user_pic).into(roomListVH.profile_image1);
                }

                if (lists.get(1).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.test_user).into(roomListVH.profile_image2);
                } else {
                    Glide.with(mContext).load(lists.get(1).user_pic).into(roomListVH.profile_image2);
                }

                if (lists.get(2).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.test_user).into(roomListVH.profile_image3);
                } else {
                    Glide.with(mContext).load(lists.get(2).user_pic).into(roomListVH.profile_image3);
                }
            } else if (lists.size() == 2) { // 2명인 경우
                roomListVH.additionalUser_tv.setVisibility(View.GONE);
                roomListVH.profile_image1.setVisibility(View.GONE);
                roomListVH.profile_image2.setVisibility(View.VISIBLE);
                roomListVH.profile_image3.setVisibility(View.VISIBLE);

                if (lists.get(0).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.test_user).into(roomListVH.profile_image2);
                } else {
                    Glide.with(mContext).load(lists.get(0).user_pic).into(roomListVH.profile_image2);
                }

                if (lists.get(1).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.test_user).into(roomListVH.profile_image3);
                } else {
                    Glide.with(mContext).load(lists.get(1).user_pic).into(roomListVH.profile_image3);
                }

            } else { // 한명인 경우
                roomListVH.additionalUser_tv.setVisibility(View.GONE);
                roomListVH.profile_image1.setVisibility(View.GONE);
                roomListVH.profile_image2.setVisibility(View.GONE);
                roomListVH.profile_image3.setVisibility(View.VISIBLE);

                if (lists.get(0).user_pic.equals("undefined")) {
                    Glide.with(mContext).load(R.drawable.test_user).into(roomListVH.profile_image3);
                } else {
                    Glide.with(mContext).load(lists.get(0).user_pic).into(roomListVH.profile_image3);
                }
            }
        }
    }

}
