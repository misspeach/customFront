package com.example.shizhan.customfront.gehui.PersonManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.shizhan.customfront.R;

/**
 * Created by gehui on 16/8/1.
 */
public class help_custom extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_help_custom);

        LinearLayout button2 = (LinearLayout) findViewById(R.id.custom_bak);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(help_custom.this, personal_help.class);
                startActivity(intent);
            }
        });
    }
}