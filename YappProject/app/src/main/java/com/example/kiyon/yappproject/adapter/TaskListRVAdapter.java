package com.example.kiyon.yappproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiyon.yappproject.Interface.OnDataChange;
import com.example.kiyon.yappproject.R;
import com.example.kiyon.yappproject.common.RetrofitServerClient;
import com.example.kiyon.yappproject.common.UserInfoReturn;
import com.example.kiyon.yappproject.model.Etc.BasicResponseResult;
import com.example.kiyon.yappproject.model.Task.TaskInfoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private String roomCaptain_id;
    private ArrayList<TaskInfoItem> taskInfoItems;
    private Date currentTime;
    private ViewGroup mRootView;
    private int taskAttendUserPostion;
    private boolean isAttendChecked = false;
    private OnDataChange onDataChange;

    public TaskListRVAdapter(Context context, ViewGroup rootView, String kakao_id) {
        mContext = context;
        taskInfoItems = new ArrayList<>();
        mRootView = rootView;
        roomCaptain_id = kakao_id;
    }

    public void setData(ArrayList<TaskInfoItem> lists) {
        taskInfoItems.clear();
        taskInfoItems.addAll(lists);
        long now = System.currentTimeMillis();
        currentTime = new Date(now); // 현재 시간 저장

        notifyDataSetChanged();
    }

    public int getTaskSize() {
        return taskInfoItems.size();
    }

    private class TaskListVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RelativeLayout taskItem_layout;
        private TextView task_title;
        private TextView task_content;
        private ImageView arrow_iv;
        private AppCompatCheckBox taskSubmit;
        private TextView taskDeadLine;
        private RecyclerView recyclerView;

        public TaskListVH(View itemView) {
            super(itemView);
            taskItem_layout = itemView.findViewById(R.id.taskItem_layout);
            task_title = itemView.findViewById(R.id.subjectName_tv);
            task_content = itemView.findViewById(R.id.subjectContent_tv);
            arrow_iv = itemView.findViewById(R.id.arrow_iv);
            taskSubmit = itemView.findViewById(R.id.check_iv);
            taskDeadLine = itemView.findViewById(R.id.time_tv);
            recyclerView = itemView.findViewById(R.id.recyclerview);

            taskItem_layout.setOnClickListener(this);
            taskSubmit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.taskItem_layout :
                    TransitionManager.beginDelayedTransition(mRootView, new ChangeBounds());
                    changeViewState();
                    break;
                case R.id.check_iv :
                    // 한번 체크하면 버튼을 비활성화 시키기 때문에 else문은 따로 필요없음.
                    if (taskSubmit.isChecked()) {
                        sendRequestApprovalToServer(getAdapterPosition());
                        taskSubmit.setButtonDrawable(R.drawable.check_1);
                        taskSubmit.setEnabled(false);
                    }
                    break;
            }
        }

        private void changeViewState() {
            if (View.GONE == recyclerView.getVisibility()) {
                arrow_iv.setImageResource(R.drawable.icn_arrow_up);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                arrow_iv.setImageResource(R.drawable.icn_arrow_down);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskListVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        isAttendChecked = false;

        TaskListVH taskListVH = (TaskListVH) holder;

        taskListVH.task_title.setText(taskInfoItems.get(position).as_name);
        taskListVH.task_content.setText(taskInfoItems.get(position).as_content);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try { // 마감시간 계산
            Date date = simpleDateFormat.parse(taskInfoItems.get(position).as_dl);
            long cal = date.getTime() - currentTime.getTime();
            long calDateDay = cal / (24*60*60*1000);
            if (calDateDay == 0) {
                taskListVH.taskDeadLine.setText(String.valueOf("D-Day"));
            } else if (calDateDay < 0) {
                taskListVH.taskDeadLine.setText("마감");
            } else {
                taskListVH.taskDeadLine.setText(String.valueOf("D-" + calDateDay));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 과제 참여인원을 보여주기위한 자식뷰 설정
        taskListVH.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        TaskAttendUsersRVAdapter taskAttendUsersRVAdapter = new TaskAttendUsersRVAdapter(mContext);
        taskListVH.recyclerView.setAdapter(taskAttendUsersRVAdapter);

        for (int i = 0; i < taskInfoItems.get(position).users.size(); i++) { // 과제아이템에 본인이 참여했는지 판별하기 위한 반복문
            if (taskInfoItems.get(position).users.get(i).user_name.equals(UserInfoReturn.getInstance().getUserName(mContext))) {
                isAttendChecked = true;
                taskAttendUserPostion = i;
                if (taskInfoItems.get(position).users.get(i).req == 1) { // 과제 승인요청을 했을 경우 버튼 클릭처리
                    taskListVH.taskSubmit.setButtonDrawable(R.drawable.check_1);
                    taskListVH.taskSubmit.setEnabled(false);
                } else { // req 값이 0일 경우
                    taskListVH.taskSubmit.setButtonDrawable(R.drawable.uncheck_1);
                    taskListVH.taskSubmit.setChecked(false);
                    taskListVH.taskSubmit.setEnabled(true);
                }
            }
        }

        if (isAttendChecked) { // 과제에 본인이 참여했으면 제출버튼 보여줌
            taskListVH.taskSubmit.setVisibility(View.VISIBLE);
        } else { // 없으면 제출버튼 삭제
            taskListVH.taskSubmit.setVisibility(View.GONE);
        }



        taskAttendUsersRVAdapter.setData(taskInfoItems.get(position).users, roomCaptain_id, onDataChange);
    }

    @Override
    public int getItemCount() {
        return taskInfoItems.size();
    }

    private void sendRequestApprovalToServer(int position) {

        // 승인 요청을 보내기 위해서 방장 id 값, 승인요청을 보내는 유저 이름이 필요하다.
        Call<BasicResponseResult> call = RetrofitServerClient.getInstance().getService().RequestApproval(roomCaptain_id, UserInfoReturn.getInstance().getUserName(mContext),
                UserInfoReturn.getInstance().getUserId(mContext), taskInfoItems.get(position).as_num);

        call.enqueue(new Callback<BasicResponseResult>() {
            @Override
            public void onResponse(Call<BasicResponseResult> call, Response<BasicResponseResult> response) {
                if (response.isSuccessful()) {
                    BasicResponseResult basicResponseResult = response.body();
                    if (basicResponseResult != null) {
                        if (basicResponseResult.answer.equals("access")) {
                            onDataChange.onChange();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseResult> call, Throwable t) {
                Toasty.error(mContext, "메시지 실패" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setOnClick(OnDataChange onClick) {
        onDataChange = onClick;
    }
}
