package com.tricker.recordmoney.model;

/**
 * Created by Tricker on 2016/9/26  026.
 */
public class User {
    public User(String name , String pwd){
        this.name= name;
        this.pwd = pwd;
    }
    private int id;
    private String name ;
    private String pwd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
