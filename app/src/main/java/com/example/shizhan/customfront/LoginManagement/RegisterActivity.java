package com.example.shizhan.customfront.LoginManagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.model.User1;
import com.example.shizhan.customfront.util.HttpManager;
import com.example.shizhan.customfront.util.NetUtil;
import com.example.shizhan.customfront.util.StringUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * Created by Administrator on 2016/7/31.
 */
public class RegisterActivity extends AppCompatActivity {
    Button btnSendCode;
    Button btnRegister;
    EditText nameEditText;
    EditText passwordEditText;
    EditText codeEditText;
    String mUsername;
    String mPassword;

    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    private int TIME = 60;//倒计时60s

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        btnRegister = (Button) findViewById(R.id.register);
        nameEditText = (EditText) findViewById(R.id.name);
        passwordEditText = (EditText) findViewById(R.id.password);
        codeEditText = (EditText) findViewById(R.id.code);
        btnSendCode = (Button) findViewById(R.id.sendCode);

        //初始化


        SMSSDK.initSDK(this, "159e2b3621402", "ea5605d402df62246b94e37cba34d001", false);

        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SMSDDK_HANDLER;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
//注册按钮
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = NetUtil.isConnected(RegisterActivity.this);
                if (!b) {
                    return;
                }
                mUsername = nameEditText.getText().toString();
                if (StringUtil.isEmpty(RegisterActivity.this, mUsername)) {
                    return;
                }
                mPassword = passwordEditText.getText().toString();
                if (StringUtil.isEmpty(RegisterActivity.this, mUsername)) {
                    return;
                }
                // doRegister(mUsername, mPassword);
                SMSSDK.submitVerificationCode("86", nameEditText.getText().toString(), codeEditText.getText().toString());
                //doRegister(nameEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });
//发送验证码按钮
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "123", Toast.LENGTH_SHORT).show();
                //发送
                btnSendCode.setEnabled(false);
                btnSendCode.setClickable(false);
                boolean b = NetUtil.isConnected(RegisterActivity.this);
                if (!b) {
                    return;
                }
                SMSSDK.getVerificationCode("86", nameEditText.getText().toString());
                TIME = 60;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 60; i > 0; i--) {
                            mHandler.sendEmptyMessage(CODE_ING);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        mHandler.sendEmptyMessage(CODE_REPEAT);
                    }
                }).start();

            }
        });
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    btnSendCode.setText("(" + --TIME + "s)后重新发送");
                    if (TIME <= 0) {
                        btnSendCode.setEnabled(true);
                        btnSendCode.setClickable(true);
                    }
                    break;
                case CODE_REPEAT://重新发送
                    TIME = 60;
                    btnSendCode.setText("获取验证码");
                    btnSendCode.setClickable(true);
                    break;
                case SMSDDK_HANDLER:


                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.e("event", "event=" + event);
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        System.out.println("--------result" + event);
                        //短信注册成功后，返回MainActivity,然后提示新好友
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                            Toast.makeText(getApplicationContext(), "验证成功", Toast.LENGTH_SHORT).show();
                            doRegister(mUsername, mPassword);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //已经验证
                            Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();


                        }
                    } else {
//				((Throwable) data).printStackTrace();
                        Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//					Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();

                    }
            }
        }
    };

    //注册
    private void doRegister(final String username, final String password) {


        String url = AppInfo.BASE_URL + "register?username=" + username + "&password=" + password;  //GET方式
        //日志
        Log.d("username", username);
        Log.d("password", password);

        try {

            new HttpManager(RegisterActivity.this).getString(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) { // 收到成功应答后会触发这里
                    try {
                        // Toast.makeText(RegisterActivity.this,"您已注册成功",Toast.LENGTH_LONG).show();
                        //解析数据，变成map格式
                        Gson gson = new Gson();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map = gson.fromJson(response, Map.class);
                        // Toast.makeText(RegisterActivity.this,"Register"+((String) map.get("message")) , Toast.LENGTH_SHORT).show();
                        if (!((String) map.get("message")).equals("success")) {
                            Toast.makeText(RegisterActivity.this, "对不起，您失败了", Toast.LENGTH_LONG).show();
                            AppInfo.setUser(null);
                        } else {
                            User1 user = new User1();
                            user.setName(username);
                            user.setPassword(password);
                            AppInfo.setUser(user);
                            Toast.makeText(RegisterActivity.this, "您已注册成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this, LoginActivity.class);
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
                    Toast.makeText(RegisterActivity.this, "注册失败" + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
