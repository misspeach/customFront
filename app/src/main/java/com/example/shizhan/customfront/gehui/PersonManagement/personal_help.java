package com.example.shizhan.customfront.gehui.PersonManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.shizhan.customfront.R;

/**
 * Created by gehui on 16/7/31.
 */
public class personal_help extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_help);

        LinearLayout button2 = (LinearLayout) findViewById(R.id.help_bk);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(personal_help.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout button3 = (LinearLayout) findViewById(R.id.help_user);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(personal_help.this, help_user.class);
                startActivity(intent);
            }
        });
        LinearLayout button4 = (LinearLayout) findViewById(R.id.help_add);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(personal_help.this, help_custom.class);
                startActivity(intent);
            }
        });
        LinearLayout button5 = (LinearLayout) findViewById(R.id.help_sign);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(personal_help.this, help_sign.class);
                startActivity(intent);
            }
        });
        LinearLayout button6 = (LinearLayout) findViewById(R.id.help_personal);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(personal_help.this, help_personal.class);
                startActivity(intent);
            }
        });
    }
}