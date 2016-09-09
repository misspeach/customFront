package com.example.shizhan.customfront.CustomManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shizhan.customfront.R;

public class CustomSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText setting_day;
    private TextView setting_time;
    private ImageView clear_day;
    private ImageView enter_time;
    private Button save_setting;
    //public static final int CustomSettingActivity=2;
    //private int position;
    private String custom_name = "";
    private String old_target_day = "";
    private String old_alarm_time = "";
    private Intent it = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_setting);

        //获取从主碎片传过来的数据
        Intent intent = getIntent();
        //position=intent.getIntExtra("postition",0);
        custom_name = intent.getStringExtra("name");
        old_target_day = intent.getStringExtra("day");
        old_alarm_time = intent.getStringExtra("time");

        initView();
        //按下返回键取消修改
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                it.putExtra("new_alarm_time", "");
                it.putExtra("new_target_day", "");
                setResult(3, it);
                finish();
            }
        });
        //
        setting_day.setHint(old_target_day);
        setting_time.setText(old_alarm_time);
        setting_day.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clear_day.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                } else {
                    clear_day.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                }
            }
        });

        clear_day.setOnClickListener(this);
        enter_time.setOnClickListener(this);
        save_setting.setOnClickListener(this);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        setting_day = (EditText) findViewById(R.id.setting_day);
        setting_time = (TextView) findViewById(R.id.setting_time);
        clear_day = (ImageView) findViewById(R.id.clear_day);
        enter_time = (ImageView) findViewById(R.id.enter);
        save_setting = (Button) findViewById(R.id.save_setting);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_day:
                setting_day.setText("");
                break;
            case R.id.enter:
                Log.i("click enter", "click enter");
                //习惯设置的活动调用提醒时间设置的活动
                Intent intent = new Intent(CustomSettingActivity.this, AlarmTimeChooseActivity.class);
                intent.putExtra("custom_name", custom_name);
                startActivityForResult(intent, 2);
                break;
            case R.id.save_setting:
                if (setting_day.getText().toString().equals(""))
                    Toast.makeText(CustomSettingActivity.this, "目标坚持天数不能为空！", Toast.LENGTH_SHORT).show();
                else {
                    //更新数据
                    //向mainFragment 返回新的目标天数和新的提醒时间
//                    if(setting_day.getText().toString().equals(""))
//                    {
//                        it.putExtra("new_target_day",setting_day.getHint().toString());
//                        Log.i(setting_day.getText().toString(),setting_day.getHint().toString());
//                    }
//                    else
                    it.putExtra("new_target_day", setting_day.getText().toString());
                    //if(setting_time.getText().toString().equals())
                    it.putExtra("new_alarm_time", setting_time.getText().toString());
                    setResult(3, it);
                    finish();
                }
        }
    }

    //接收提醒时间设置活动传递过来数据的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setting_time.setText(data.getStringExtra("alarmTime"));
            Log.i("from alarmTimechoose", data.getStringExtra("alarmTime"));
        }
    }
}
