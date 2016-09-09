package com.example.shizhan.customfront.gehui.PersonManagement;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.shizhan.customfront.LoginManagement.LoginActivity;
import com.example.shizhan.customfront.MainActivity;
import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.model.User;

/**
 * Created by gehui on 16/7/28.
 */
public class SettingActivity extends Activity {


    User user = null; //用于传递数据

    private CircleImageView im_setting_image; //显示图片setting_image

    LinearLayout button2;//用于返回

    public void setHeadPortrait() {

        if (user == null)
            return;
        Bitmap bitm = NativeData.openBitmap(user.getImage());
        if (bitm != null)
            im_setting_image.setImageBitmap(bitm);

    }

    public void ReceviUserData() {
        Intent intent = this.getIntent();
        user = (User) intent.getSerializableExtra("user");
        //user=(User) intent.getSerializableExtra("")
    }
//    public void SendUserData(User user){
//        Intent intent = new Intent(SettingActivity.this, EditActivity.class);
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("user",user);//序列化
//        intent.putExtras(bundle);//发送数据
//        startActivity(intent);//启动intent
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ReceviUserData(); //接收数据


        setContentView(R.layout.personal_setting);

        im_setting_image = (CircleImageView) findViewById(R.id.Setting_image);

        button2 = (LinearLayout) findViewById(R.id.title_back);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跟登录界面学的传点东西回去
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, MainActivity.class);
                intent.putExtra("user_name", user.getUser_name());
                intent.putExtra("user_id", String.valueOf(user.getId()));
                startActivity(intent);
                finish();

                //  Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                //  startActivity(intent);
                // finish();
            }
        });

        LinearLayout button3 = (LinearLayout) findViewById(R.id.setting_personal);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SettingActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);//序列化
                intent.putExtras(bundle);//发送数据
                startActivity(intent);//启动intent

                // SendUserData(user);
                // Intent intent = new Intent(SettingActivity.this, EditActivity.class); startActivity(intent);
            }
        });

        LinearLayout button4 = (LinearLayout) findViewById(R.id.setting_protocol);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, personal_protocol.class);
                startActivity(intent);
            }
        });

        LinearLayout button5 = (LinearLayout) findViewById(R.id.setting_about);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, personal_about.class);
                startActivity(intent);
            }
        });

        LinearLayout button6 = (LinearLayout) findViewById(R.id.setting_help);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, personal_help.class);
                startActivity(intent);
            }
        });
//        退出登陆，跳转到登陆页面
        LinearLayout button7 = (LinearLayout) findViewById(R.id.setting_quit);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                System.exit(0); //  不是本段程序中，退出进程
            }
        });


        setHeadPortrait();


    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            button2.performClick();

        }

        return false;

    }

}

