package com.example.shizhan.customfront.calendar;

import java.io.Serializable;

/**
 * Created by candy on 2016/8/2.
 */

public class Custom implements Serializable {
    public int year;
    public int month;
    public int day;

    public Custom(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
