package com.team_yapp.kiyon.yappproject.model.Task;

import java.io.Serializable;

public class TaskStatisticsResponseResult implements Serializable {
    public int accept; // 승인여부
    public int late; // 지각여부
    public String as_name; // 과제 이름

}
