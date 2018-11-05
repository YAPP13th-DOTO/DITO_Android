package com.example.kiyon.yappproject.common;

import com.example.kiyon.yappproject.model.AddTaskResponseResult;
import com.example.kiyon.yappproject.model.BasicResponseResult;
import com.example.kiyon.yappproject.model.CreateRoomResponseResult;
import com.example.kiyon.yappproject.model.LoginResponseResult;
import com.example.kiyon.yappproject.model.RoomList.RoomListResponseResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // 로그인
    @GET("login")
    Call<LoginResponseResult> LoginResponseResult (
            @Query("id") String id, @Query("name") String name, @Query("val") String image
    );

    // 회원 정보 확인
    @GET("get/user")
    Call<LoginResponseResult> ConfirmResponseResult (
            @Query("id") String id
    );

    // 방생성
    @GET("create")
    Call<CreateRoomResponseResult> CreateRoomResponseResult (
            @Query("id") String id, @Query("tname") String tname, @Query("sname") String sname
    );

    // 방목록 조회
    @GET("get")
    Call<ArrayList<RoomListResponseResult>> RoomListResponseResult (
            @Query("id") String id
    );

    // 방참여
    @GET("attend")
    Call<BasicResponseResult> RoomAttend (
      @Query("code") String code, @Query("id") String id
    );

    //과제생성
    @GET("get/team")
    Call<AddTaskResponseResult> AddTaskResponesResult (
            @Query("tmcode") String tmcode
    );
}
