package com.example.shizhan.customfront.gehui.PersonManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.model.User;

/**
 * Created by diannao on 16/8/4.
 */
public class edit_password extends Activity {


    User user = null; //用于传递数据
    private TextView text_edit_pd1;
    private TextView text_edit_pd2;
    private TextView text_edit_pd3;
    private Button button_b_confirm;
    private LinearLayout button2;


    public void ReceviUserData() {
        Intent intent = this.getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_password);
        ReceviUserData();
        button2 = (LinearLayout) findViewById(R.id.passord_back);


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //把数据再次传回去
                Intent intent = new Intent(edit_password.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);//序列化
                intent.putExtras(bundle);//发送数据
                startActivity(intent);//启动intent
                // Intent intent = new Intent(edit_password.this, EditActivity.class); startActivity(intent);
            }
        });
        text_edit_pd1 = (TextView) findViewById(R.id.edit_pd1);
        text_edit_pd2 = (TextView) findViewById(R.id.edit_pd2);
        text_edit_pd3 = (TextView) findViewById(R.id.edit_pd3);

        button_b_confirm = (Button) findViewById(R.id.b_confirm);
        button_b_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //把数据再次传回去
                if (user.getPassword() == null) {
                    String s = text_edit_pd1.getText().toString();
                    if (!(s.equals(""))) {
                        Toast.makeText(edit_password.this, "新用户不必输入旧密码", Toast.LENGTH_SHORT).show();
                        return;
                    }


                } else {
                    if (!(user.getPassword().equals(text_edit_pd1.getText().toString()))) {
                        Toast.makeText(edit_password.this, "旧密码错误", Toast.LENGTH_SHORT).show();
                        return;
                    }


                }

                String s2 = text_edit_pd2.getText().toString();
                String s3 = text_edit_pd3.getText().toString();

                if (!(s2.equals(s3))) {

                    Toast.makeText(edit_password.this, "两次设置的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.setPassword(text_edit_pd2.getText().toString());

                NativeData native_data;
                native_data = new NativeData(user);

                boolean ret = native_data.changeUserInfomation();
                if (ret)
                    Toast.makeText(edit_password.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(edit_password.this, "修改密码失败", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(edit_password.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);//序列化
                intent.putExtras(bundle);//发送数据

//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable(){public void run() { //延时一会
//
//                    }   },1000);


                startActivity(intent);//启动intent
                // Intent intent = new Intent(edit_password.this, EditActivity.class); startActivity(intent);
            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            button2.performClick();

        }

        return false;

    }


}