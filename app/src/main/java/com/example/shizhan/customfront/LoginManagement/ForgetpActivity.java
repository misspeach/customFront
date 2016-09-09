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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.util.HttpManager;
import com.example.shizhan.customfront.util.NetUtil;
import com.example.shizhan.customfront.util.StringUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/8/7.
 */
public class ForgetpActivity extends AppCompatActivity {

    Button bnSendCode;
    Button bnRegister;
    Button bnLogin_back;
    EditText nameEditTexts;
    EditText codeEditTexts;
    String mUsername;
    String mPassword;
    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    private int TIME = 60;//倒计时60s
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_forgetp);
        textView = (TextView) findViewById(R.id.fgt_password);


        bnRegister = (Button) findViewById(R.id.registerF);
        nameEditTexts = (EditText) findViewById(R.id.nameF);

        codeEditTexts = (EditText) findViewById(R.id.codeF);
        bnSendCode = (Button) findViewById(R.id.sendCodeF);

        bnLogin_back = (Button) findViewById(R.id.login_back);

        //初始化
        //短信验证
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
        bnLogin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = NetUtil.isConnected(ForgetpActivity.this);
                if (!b) {
                    return;
                }
                //User u = new User (idEditText.getText().toString(),nameEditText.getText().toString(),passwordEditText.getText().toString(),phoneEditText.getText().toString());
                Intent intent = new Intent();
                intent.setClass(ForgetpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //取回密码按钮
        bnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean b = NetUtil.isConnected(ForgetpActivity.this);
                if (!b) {
                    return;
                }

                mUsername = nameEditTexts.getText().toString();
                if (StringUtil.isEmpty(ForgetpActivity.this, mUsername)) {
                    return;
                }
                //doForgetp(mUsername);

                SMSSDK.submitVerificationCode("86", nameEditTexts.getText().toString(), codeEditTexts.getText().toString());
                //doRegister(nameEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });

//发送验证码按钮
        bnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "123", Toast.LENGTH_SHORT).show();
                //发送
                bnSendCode.setEnabled(false);
                bnSendCode.setClickable(false);
                boolean b = NetUtil.isConnected(ForgetpActivity.this);
                if (!b) {
                    return;
                }
                SMSSDK.getVerificationCode("86", nameEditTexts.getText().toString());
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

    //计时器
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    bnSendCode.setText("(" + --TIME + "s)后重新发送");
                    if (TIME <= 0) {
                        bnSendCode.setEnabled(true);
                        bnSendCode.setClickable(true);
                    }
                    break;
                case CODE_REPEAT://重新发送
                    bnSendCode.setText("获取验证码");
                    bnSendCode.setClickable(true);
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
                            doForgetp(mUsername);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //已经验证
                            Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();


                        }
                    } else {
//				((Throwable) data).printStackTrace();
                        Toast.makeText(ForgetpActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//					Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();

                    }
            }
        }
    };

    //取回密码
    private void doForgetp(final String username) {


        String url = AppInfo.BASE_URL + "forgetp?username=" + username;  //GET方式
        //日志
        Log.d("username", username);


        try {

            new HttpManager(ForgetpActivity.this).getString(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) { // 收到成功应答后会触发这里
                    try {
                        // Toast.makeText(RegisterActivity.this,"您已注册成功",Toast.LENGTH_LONG).show();
                        //解析数据，变成map格式
                        Gson gson = new Gson();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map = gson.fromJson(response, Map.class);
                        if (!((String) map.get("message")).equals("success")) {

                            AppInfo.setUser(null);
                            textView.setText("");
                        } else {
                            //Toast.makeText(ForgetpActivity.this,"您已取回密码",Toast.LENGTH_LONG).show();
                            textView.setText("您的密码为：" + (String) map.get("password"));

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    AppInfo.setUser(null);
                    Toast.makeText(ForgetpActivity.this, "取回密码失败", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
