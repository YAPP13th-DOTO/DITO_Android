package com.team_yapp.kiyon.yappproject.model.Room;

import com.team_yapp.kiyon.yappproject.model.Etc.PercentResponseResult;
import com.team_yapp.kiyon.yappproject.model.Task.TaskStatisticsResponseResult;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomStatisticsResponseResult implements Serializable{

    public String kakao_id; // 사용자 id
    public String user_name;
    public String user_pic;
    public ArrayList<PercentResponseResult> percent; // 통계 퍼센트

    public ArrayList<TaskStatisticsResponseResult> as_name; // 유저참여한 과제 목록
}
