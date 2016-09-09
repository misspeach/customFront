package com.example.shizhan.customfront.LoginManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.shizhan.customfront.MainActivity;
import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.model.User1;
import com.example.shizhan.customfront.util.HttpManager;
import com.example.shizhan.customfront.util.NetUtil;
import com.example.shizhan.customfront.util.StringUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    String mUsername;
    String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        Button bn = (Button) findViewById(R.id.btn_login);
        TextView textView = (TextView) findViewById(R.id.btn_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = NetUtil.isConnected(LoginActivity.this);
                if (!b) {
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        TextView textview = (TextView) findViewById(R.id.tv_forgetPassword);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = NetUtil.isConnected(LoginActivity.this);
                if (!b) {
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, ForgetpActivity.class);
                startActivity(intent);
            }
        });

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = NetUtil.isConnected(LoginActivity.this);
                if (!b) {
                    return;
                }
                EditText nameEditText = (EditText) findViewById(R.id.edit_account);
                mUsername = nameEditText.getText().toString();
                if (StringUtil.isEmpty(LoginActivity.this, mUsername)) {
                    return;
                }

                EditText passwordEditText = (EditText) findViewById(R.id.edit_password);
                mPassword = passwordEditText.getText().toString();
                if (StringUtil.isEmpty(LoginActivity.this, mPassword)) {
                    return;
                }

                //User u = new User (idEditText.getText().toString(),nameEditText.getText().toString(),passwordEditText.getText().toString(),phoneEditText.getText().toString());
                doLogin(nameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });


    }

    // 定义发送请求的方法
    private void doLogin(final String username, final String password) {


        String url = AppInfo.BASE_URL + "login?username=" + username + "&password=" + password;  //GET方式
        //日志
        Log.d("username", username);
        Log.d("password", password);

        try {

            new HttpManager(LoginActivity.this).getString(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) { // 收到成功应答后会触发这里
                    try {
                        // Toast.makeText(RegisterActivity.this,"您已注册成功",Toast.LENGTH_LONG).show();
                        //解析数据，变成map格式
                        Gson gson = new Gson();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map = gson.fromJson(response, Map.class);
                        if (!((String) map.get("message")).equals("success")) {
                            Toast.makeText(LoginActivity.this, "对不起，您登录失败了", Toast.LENGTH_LONG).show();
                            AppInfo.setUser(null);
                        } else {
                            User1 user = new User1();
                            String id = (String) map.get("id");
                            user.setId(id);
                            user.setName(username);
                            user.setPassword(password);
                            AppInfo.setUser(user);
                            Toast.makeText(LoginActivity.this, "您已登录成功", Toast.LENGTH_LONG).show();
                            //传用户名给MainActivity
                            //id="5";
                            Log.i("Login_user_name", username);
                            Log.i("Login_user_id", id);
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user_name", username);
                            intent.putExtra("user_id", id);
                            startActivity(intent);
                            finish();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    AppInfo.setUser(null);
                    Toast.makeText(LoginActivity.this, "登录失败" + error.getMessage(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
