package com.example.shizhan.customfront.CustomManagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.shizhan.customfront.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.util.WheelUtils;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

public class AlarmTimeChooseActivity extends AppCompatActivity {

    private WheelView hourWheelView, minuteWheelView;
    private String hour = "12", minute = "00", time = "";
    private View layout;
    private TextView custom_name;
    private int checked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_time_choose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        //设置返回键,返回设置的提醒时间给CustomsettingActivity
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked == 0)
                    time = hour + ":" + minute;
                Intent intent = new Intent();
                Log.i("AlarmTimeChooseActivity", time);
                intent.putExtra("alarmTime", time);
                setResult(RESULT_OK, intent);
                finish();//销毁当前活动
            }
        });

        //得到传过来的习惯名并显示
        Intent intent = getIntent();
        custom_name = (TextView) findViewById(R.id.custom_show_name);
        custom_name.setText(intent.getStringExtra("custom_name"));

        initWheel();
        layout = findViewById(R.id.show_alarm);
        ToggleButton mTogBtn = (ToggleButton) findViewById(R.id.mTogBtn); // 获取到控件
        mTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //选中 展示布局 提醒
                    layout.setVisibility(View.VISIBLE);
                    time = hour + ":" + minute;
                } else {
                    //未选中 收起布局 不提醒
                    checked = 1;
                    layout.setVisibility(View.GONE);
                    time = "不提醒";
                }
            }
        });// 添加监听事件
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        time = hour + ":" + minute;
        Intent intent = new Intent();
        intent.putExtra("alarmTime", time);
        Log.i("AlarmTimeChooseActivity", time);
        setResult(RESULT_OK, intent);
        finish();//销毁当前活动
    }

    private void initWheel() {
        //设置小时滚轮
        hourWheelView = (WheelView) findViewById(R.id.hour_wheelview);
        hourWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        hourWheelView.setSkin(WheelView.Skin.None);
        hourWheelView.setWheelData(createHours());
        //设置一种风格
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#d71345");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;

        hourWheelView.setStyle(style);
        hourWheelView.setExtraText("时", Color.parseColor("#d71345"), 40, 70);

        //先设置可以循环，再设置滚轮位置，不然位置无效
        hourWheelView.setLoop(true);
        hourWheelView.setSelection(12);
        //可以点击
        hourWheelView.setWheelClickable(true);

        hourWheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onItemClick(int position, Object o) {
                WheelUtils.log("click:" + position + "," + o.toString());
                if (!hour.equals(o.toString()))
                    hour = o.toString();
            }
        });
        //滚动时选中
        hourWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int position, String data) {
                WheelUtils.log("selected:" + position + "," + data);
                if (!hour.equals(data))
                    hour = data;
            }
        });

        minuteWheelView = (WheelView) findViewById(R.id.minute_wheelview);
        minuteWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        minuteWheelView.setSkin(WheelView.Skin.None);
        minuteWheelView.setWheelData(createMinutes());

        minuteWheelView.setStyle(style);
        minuteWheelView.setExtraText("分", Color.parseColor("#d71345"), 40, 70);
        minuteWheelView.setSelection(00);
        //可以循环
        minuteWheelView.setLoop(true);
        //可以点击
        minuteWheelView.setWheelClickable(true);

        minuteWheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onItemClick(int position, Object o) {
                WheelUtils.log("click:" + position);
                if (!minute.equals(o.toString()))
                    minute = o.toString();
            }
        });
        //滚动时选中
        minuteWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int position, String data) {
                WheelUtils.log("selected:" + position + "," + data);
                if (!minute.equals(data))
                    minute = data;
            }
        });

    }

    private ArrayList<String> createHours() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }
}
