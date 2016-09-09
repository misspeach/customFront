package com.example.shizhan.customfront.LoginManagement;

import com.example.shizhan.customfront.model.User1;

/**
 * Created by Administrator on 2016/8/4.
 */
//获取用户名
public class AppInfo {
    public static User1 mUser = null;

    public static User1 getUser() {
        return mUser;
    }

    public static void setUser(User1 user) {
        mUser = user;
    }

    //IP地址
    public static String BASE_URL = "http://192.168.1.100:8080/";
}
