package com.test.config;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

    private int userId;
    private String userName;
    private Date birthday;

    public User() {
    }

    public User(int userId, String userName, Date birthday) {
        this.userId = userId;
        this.userName = userName;
        this.birthday = birthday;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(birthday);
        return String.format("User{userId = %d, userName = %s, birthday = %s}", userId, userName, date);
    }
}
