package com.team_yapp.kiyon.yappproject.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.team_yapp.kiyon.yappproject.common.CustomTypefaceSpan;
import com.team_yapp.kiyon.yappproject.Interface.OnDataChange;
import com.team_yapp.kiyon.yappproject.R;
import com.team_yapp.kiyon.yappproject.common.RetrofitServerClient;
import com.team_yapp.kiyon.yappproject.common.UserInfoReturn;
import com.team_yapp.kiyon.yappproject.model.Etc.BasicResponseResult;
import com.team_yapp.kiyon.yappproject.model.Task.TaskAttendUsersItem;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAttendUsersRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TaskAttendUsersItem> taskAttendUsersItems;
    private String roomCaptain_id;
    private OnDataChange onDataChange;

    public TaskAttendUsersRVAdapter(Context context) {
        mContext = context;
        taskAttendUsersItems = new ArrayList<>();
    }

    public void setData(ArrayList<TaskAttendUsersItem> lists, String captain_id, OnDataChange onDataChange) {
        taskAttendUsersItems.clear();
        taskAttendUsersItems.addAll(lists);
        roomCaptain_id = captain_id;
        this.onDataChange = onDataChange;
        notifyDataSetChanged();
    }

    private class  TaskAttendUsersVH extends RecyclerView.ViewHolder {
        private ImageView userImage;
        private TextView userName;
        private TextView lateStatus;
        private RelativeLayout submitStatus_layout;
        private ImageView taskApproval_ok;
        private ImageView taskApproval_cancel;
        private TextView submitStatus;

        public TaskAttendUsersVH(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.profile_iv);
            userName = itemView.findViewById(R.id.userName_tv);
            lateStatus = itemView.findViewById(R.id.late_status_tv);
            submitStatus_layout = itemView.findViewById(R.id.status_layout);
            taskApproval_ok = itemView.findViewById(R.id.approval_ok);
            taskApproval_cancel = itemView.findViewById(R.id.approval_cancel);
            submitStatus = itemView.findViewById(R.id.submit_status_tv);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskAttendUsersVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_task_attenduser, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        TaskAttendUsersVH taskAttendUsersVH = (TaskAttendUsersVH) holder;

        if (taskAttendUsersItems.get(position).user_pic.equals("undefined")) {
            Glide.with(mContext).load(R.drawable.temp_user_image).into(taskAttendUsersVH.userImage);
        } else {
            Glide.with(mContext).load(taskAttendUsersItems.get(position).user_pic).into(taskAttendUsersVH.userImage);
        }

        taskAttendUsersVH.userName.setText(taskAttendUsersItems.get(position).user_name);

        // 지각 유무 판단!
        if (taskAttendUsersItems.get(position).late == 0) {
            taskAttendUsersVH.lateStatus.setText("");
        } else {
            taskAttendUsersVH.lateStatus.setText("지각");
        }

//        & 제출 상태에 대한 경우의 수
//        * 변수명 : req
//        - 승인요청을 유무 값 (1 또는 0)
//        * 변수 명 : accept
//        - 방장 승인 유무 값 (1 또는 0)
//
//        * 경우의 수 (11 10 01 00)
//        - req : 1 accept : 1일 경우
//        유저화면 : (제출완료)
//        방장화면 : (제출완료)
//
//        -req : 1 accept : 0일 경우
//        유저화면 : (승인대기중)
//        방장화면 : o x
//
//        - req : 0 accept : 1 일 경우
//        이런 경우의 수는 없음
//
//        - req : 0 accept : 0일경우
//        유저화면 : 미제출
//        방장화면 : 미제출
        if (taskAttendUsersItems.get(position).req == 0 && taskAttendUsersItems.get(position).accept == 0) {
            taskAttendUsersVH.submitStatus_layout.setVisibility(View.GONE);
            taskAttendUsersVH.submitStatus.setVisibility(View.VISIBLE);
            taskAttendUsersVH.submitStatus.setText("미제출");
        } else if (taskAttendUsersItems.get(position).req == 1 && taskAttendUsersItems.get(position).accept == 0) {
            if (UserInfoReturn.getInstance().getUserId(mContext).equals(roomCaptain_id)) { // 앱 사용자가 방을 만든 방장일 경우
                taskAttendUsersVH.submitStatus_layout.setVisibility(View.VISIBLE);
                taskAttendUsersVH.submitStatus.setVisibility(View.GONE);
            } else { // 앱 사용자가 일반 유저일 경우
                taskAttendUsersVH.submitStatus_layout.setVisibility(View.GONE);
                taskAttendUsersVH.submitStatus.setVisibility(View.VISIBLE);
                taskAttendUsersVH.submitStatus.setText("승인대기중");
            }
        } else if (taskAttendUsersItems.get(position).req == 1 && taskAttendUsersItems.get(position).accept == 1) {
            taskAttendUsersVH.submitStatus_layout.setVisibility(View.GONE);
            taskAttendUsersVH.submitStatus.setVisibility(View.VISIBLE);
            taskAttendUsersVH.submitStatus.setText("제출완료");
        }

        // 과제 제출 승인완료
        taskAttendUsersVH.taskApproval_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialogView(1, 1, position); // 승인 버튼을 눌렀을 경우
            }
        });

        // 과제 제출 승인거절
        taskAttendUsersVH.taskApproval_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialogView(0, 0, position); // 거절 버튼을 눌렀을 경우
            }
        });
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return taskAttendUsersItems.size();
    }

    private void sendResponseApprovalToServer(int accept, int position) { // accept : 방장이 승인할경우 1값 , 아닐경우 0값

        Call<BasicResponseResult> call = RetrofitServerClient.getInstance().getService().ResponseApproval(taskAttendUsersItems.get(position).kakao_id,
                accept, taskAttendUsersItems.get(position).as_num);
        call.enqueue(new Callback<BasicResponseResult>() {
            @Override
            public void onResponse(Call<BasicResponseResult> call, Response<BasicResponseResult> response) {
                // 승인 요청 완료
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
                Toasty.error(mContext, "메시지 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // accept : 승인(1) 승인거절(0)
    private void setDialogView(int type, final int accept, final int position) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_requestapproval);

        TextView textView = dialog.findViewById(R.id.text);
        Typeface nanumBoldFont = Typeface.createFromAsset(mContext.getAssets(), "nanumbarungothicbold.ttf"); // 특정 text 폰트 적용

        if (type == 1) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("제출요청을승인하시겠습니까?");
            spannableStringBuilder.insert(5, "\n");
            spannableStringBuilder.setSpan (new CustomTypefaceSpan("", nanumBoldFont), 5, 8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            textView.setText(spannableStringBuilder);
        } else {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("제출요청을거절하시겠습니까?");
            spannableStringBuilder.insert(5, "\n");
            spannableStringBuilder.setSpan (new CustomTypefaceSpan("", nanumBoldFont), 5, 8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            textView.setText(spannableStringBuilder);
        }



        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dialog.findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.ok_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponseApprovalToServer(accept, position);
                dialog.dismiss();
            }
        });

    }
}
