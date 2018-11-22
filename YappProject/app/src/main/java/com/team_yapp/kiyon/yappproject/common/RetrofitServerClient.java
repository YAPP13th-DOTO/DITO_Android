package com.team_yapp.kiyon.yappproject.common;

import com.team_yapp.kiyon.yappproject.Interface.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServerClient {

    private static final String BASE_URL = "http://ec2-13-125-142-135.ap-northeast-2.compute.amazonaws.com:3000/";
    private static RetrofitServerClient instance = null;

    public static RetrofitServerClient getInstance() {
        if (instance == null) {
            instance = new RetrofitServerClient();
        }
        return instance;
    }

    private RetrofitServerClient() {

    }

    public ApiService getService() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder() // 네트워크 타임아웃 시간 설정
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        return service;
    }
}
