package com.example.kiyon.yappproject.model.RoomList;

import java.io.Serializable;

public class UserResponseResult implements Serializable {

    public String user_name;
    public String kakao_id; // user 고유 id값
    public String user_pic;
    public int iscreater; // 해당 변수가 1일 경우 방장유저 0일 경우 일반 유저
    public boolean isUserChecked = false;

}
