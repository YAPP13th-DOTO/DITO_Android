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
import com.example.kiyon.yappproject.model.Room.UserResponseResult;

import java.util.ArrayList;

public class MemberListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<UserResponseResult> userResponseResults;
    private Context context;
    private MemberListRVAdapter.ItemClickListener itemClickListener;

    public MemberListRVAdapter(Context context) {
        this.context = context;
        userResponseResults = new ArrayList<>();
    }
    public void setData(ArrayList<UserResponseResult> lists) {
        userResponseResults.clear();
        userResponseResults.addAll(lists);
        notifyDataSetChanged();
    }
    public class MemberListVH extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView userName;
        private CheckBox checkBox;

        public MemberListVH(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.userName);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_member_list,parent,false);
        return new MemberListVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MemberListVH memberListVH = (MemberListVH) holder;

        if(userResponseResults.get(position).user_pic.equals("undefined")) {
            Glide.with(context).load(R.drawable.test_user).into(memberListVH.profileImage);
        }else {
            Glide.with(context).load(userResponseResults.get(position).user_pic).into(memberListVH.profileImage);
        }

        memberListVH.userName.setText(userResponseResults.get(position).user_name);
        
        memberListVH.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(memberListVH.checkBox.isChecked()) {
                    if(itemClickListener!=null) itemClickListener.onItemClick(position,b,userResponseResults.get(position));
                    memberListVH.checkBox.setButtonDrawable(R.drawable.check);
                } else {
                    if(itemClickListener!=null) itemClickListener.onItemClick(position,b,userResponseResults.get(position));
                    memberListVH.checkBox.setButtonDrawable(R.drawable.un_check);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userResponseResults.size();
    }

    public void setClickListener(MemberListRVAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int pos, boolean check, UserResponseResult person);
    }
}
