package com.example.shizhan.customfront.gehui.PersonManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.shizhan.customfront.R;

/**
 * Created by gehui on 16/7/26.
 */
public class GeHuiMainActivity extends Activity {
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_index);


        LinearLayout button1 = (LinearLayout) findViewById(R.id.title_edit);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GeHuiMainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
}
