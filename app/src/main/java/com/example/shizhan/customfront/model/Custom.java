package com.example.shizhan.customfront.model;

/**
 * Created by shizhan on 16/7/25.
 */
public class Custom {
    //习惯ID
    private Long custom_Id;
    //习惯名称
    private String custom_name;
    //提醒时间
    private String alarm_time;
    //当前完成天数
    private String insist_day;
    //    //分类图片地址
//    private String image_url;
    //习惯分类
    private String category;
    //目标天数
    private String target_day;
    //最大连续天数
    private String max_insist_day;
    //当前连续天数
    private String current_insist_day;
    //用户ID
    private Long user_Id;
    //是否打卡
    private int isRecorded;


    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getInsist_day() {
        return insist_day;
    }

    public void setInsist_day(String insist_day) {
        this.insist_day = insist_day;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTarget_day() {
        return target_day;
    }

    public void setTarget_day(String target_day) {
        this.target_day = target_day;
    }

    public String getMax_insist_day() {
        return max_insist_day;
    }

    public void setMax_insist_day(String max_insist_day) {
        this.max_insist_day = max_insist_day;
    }

    public String getCurrent_insist_day() {
        return current_insist_day;
    }

    public void setCurrent_insist_day(String current_insist_day) {
        this.current_insist_day = current_insist_day;
    }

    public Long getCustom_Id() {
        return custom_Id;
    }

    public void setCustom_Id(Long custom_Id) {
        this.custom_Id = custom_Id;
    }

    public Long getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(Long user_Id) {
        this.user_Id = user_Id;
    }

    public int isRecorded() {
        return isRecorded;
    }

    public void setRecorded(int recorded) {
        isRecorded = recorded;
    }
}
