package com.example.kiyon.yappproject.common;

import com.example.kiyon.yappproject.model.Etc.BasicResponseResult;
import com.example.kiyon.yappproject.model.Room.CreateRoomResponseResult;
import com.example.kiyon.yappproject.model.Etc.LoginResponseResult;
import com.example.kiyon.yappproject.model.Room.RoomListResponseResult;
import com.example.kiyon.yappproject.model.Task.TaskListResponseResult;


import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    //과제 생성
    @FormUrlEncoded
    @POST("create/assign")
    Call<BasicResponseResult> AddTaskResponseResult (
            @Field("tmcode") String tmcode,
            @Field("asname") String asname,
            @Field("ascontent") String ascontent,
            @Field("asdl") String asdl,
            @Field("users") ArrayList<String> users
    );

    // 방안에 있는 과제리스트 조회
    @GET("get/team/assign")
    Call<TaskListResponseResult> TaskListResponseResult (
            @Query("tmcode") String tmcode
    );

    // 서버에 토큰 저장
    @FormUrlEncoded
    @POST("token")
    Call<ResponseBody> TokenResponseResult (
            @Field("token") String token, @Field("id") String id
    );

    // 유저 과제 승인요청
    @GET("push/req")
    Call<ResponseBody> RequestApprovalResponseResult (
            @Query("id") String id, @Query("name") String name
    );

}
