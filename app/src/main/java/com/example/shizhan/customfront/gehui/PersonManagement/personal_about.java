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
public class personal_about extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_about);

        LinearLayout button2 = (LinearLayout) findViewById(R.id.about_bk);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(personal_about.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
}
