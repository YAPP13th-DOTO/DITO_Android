package com.example.kiyon.yappproject.common;

import com.example.kiyon.yappproject.model.LoginResponseResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    // 이메일 , 이미지, 닉네임
    @GET("login")
    Call<LoginResponseResult> LoginResponseResult (
            @Query("id") String id, @Query("name") String name, @Query("val") String image
    );
}
