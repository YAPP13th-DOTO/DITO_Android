package com.example.kiyon.yappproject.model;

public class AddTaskResponseResult {

    public String tmcode;
    public String asname;
    public String ascontent;
    public String asdl;

    AddTaskResponseResult(String tmcode, String asname, String ascontent, String asdl) {
        this.tmcode = tmcode;
        this.asname = asname;
        this.ascontent = ascontent;
        this.asdl = asdl;
    }
}
