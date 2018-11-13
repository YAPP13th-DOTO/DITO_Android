package com.example.kiyon.yappproject.model.Task;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskInfoItem implements Serializable {

    public String tm_code; // 방 코드
    public String as_content; // 과제 내용
    public String as_name; // 과제 이름
    public int as_num; // 과제 key 값
    public String as_dl; // 과제 마감기한

    public ArrayList<TaskAttendUsersItem> users;

}
