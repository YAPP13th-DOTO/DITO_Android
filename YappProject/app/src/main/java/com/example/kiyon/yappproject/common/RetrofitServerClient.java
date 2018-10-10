package com.example.kiyon.yappproject.common;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        return service;
    }
}
