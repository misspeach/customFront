package com.example.shizhan.customfront.model;

/**
 * Created byLiXueqiao on 2016/8/3.
 */
public class User1 {

    private String id;
    private String name;
    private String password;
    private String phone;

    public User1() {
    }

    public User1(String id, String name, String password, String phone) {
        this.id = id;
        this.name = name;
        this.password = password;

        this.phone = phone;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
