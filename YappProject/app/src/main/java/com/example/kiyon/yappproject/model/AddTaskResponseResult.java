package com.example.kiyon.yappproject.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddTaskResponseResult {

    public String tmcode;
    public String asname;
    public String ascontent;
    public String asdl;
    public ArrayList<String> users;

    AddTaskResponseResult(String tmcode, String asname, String ascontent, String asdl, ArrayList<String> users) {
        this.tmcode = tmcode;
        this.asname = asname;
        this.ascontent = ascontent;
        this.asdl = asdl;
        this.users = users;
    }
}
