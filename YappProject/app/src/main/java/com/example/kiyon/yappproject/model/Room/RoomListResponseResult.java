package com.example.kiyon.yappproject.model.Room;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomListResponseResult implements Serializable {

    public String tm_code; // 방 코드
    public int iscreater; // 생성자 인지 판별
    public String tm_name; // 방 이름
    public String sub_name;  // 과목 이름
    public int isdone; // 방이 종료 됬는지 판별
    public String date; // 생성 날짜

    public ArrayList<RoomAttendUsersItem> users;

}
