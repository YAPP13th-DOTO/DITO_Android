package com.example.kiyon.yappproject.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class UserInfoReturn {

    private static UserInfoReturn instance = null;

    public static UserInfoReturn getInstance() {
        if (instance == null) {
            instance = new UserInfoReturn();
        }
        return instance;
    }

    private UserInfoReturn() {

    }

    public String getUserName(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("DITO", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", null);

        return userName;
    }

    public String getUserId(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("DITO", MODE_PRIVATE);
        String userID = sharedPreferences.getString("userID", null);

        return userID;
    }
}
