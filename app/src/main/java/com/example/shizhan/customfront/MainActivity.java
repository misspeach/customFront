package com.example.shizhan.customfront;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shizhan.customfront.CustomManagement.AddActivity;
import com.example.shizhan.customfront.CustomManagement.MainFragment;
import com.example.shizhan.customfront.gehui.PersonManagement.PersonalFragment;
import com.example.shizhan.customfront.model.Custom;
import com.example.shizhan.customfront.util.Callback;
import com.example.shizhan.customfront.util.ExampleUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "MainActivity";

    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;

    //Fragment Object
    private MainFragment main_fg;
    private PersonalFragment fg3;

    private FragmentManager fManager;
    private static final int MainActivity = 1;
    private FragmentTransaction fTransaction;

    //test
    private String custom_name;
    private String alarm_time;
    private String target_day;
    private String category;
    private Custom custom;

    private String user_name = "";
    private String user_id = "";

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getSupportActionBar().hide();//隐藏标题栏，API版本24
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//API版本21

        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");

       /*
       * test 假设已经得到了LoginActivity中的数据
       * */
//        user_name="shizhan";
//        user_id="1";
//        user_name="13504040587";
//        user_id="5";
        //调用JPush API设置Alias 用户名
        //String alias=user_name;
        //mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));

        //得到LoginActivity中传过来的数据,在设置单选按钮之前得到数据！！！！！！！！！！！！
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        user_id = intent.getStringExtra("user_id");
        Log.i("Mainactivity_user_name", user_name);
        Log.i("Mainactivity_user_id", user_id);

        String alias = user_name;
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));

        //test
        //user_name=

        // 初始化Jpush
        init();

        fManager = getFragmentManager();
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        //获取第一个单选按钮，并设置其为选中状态
        rb_channel = (RadioButton) findViewById(R.id.rb_custom);
        rb_channel.setChecked(true);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        fTransaction = fManager.beginTransaction();//必须放在这个方法内不能放在外面
        hideAllFragment(fTransaction);
        switch (checkedId) {
            case R.id.rb_custom:
                if (main_fg == null) {
                    main_fg = new MainFragment();
                    //把用户名和密码传给MainFragment
//                    Log.i(user_name,user_id);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_name", user_name);
                    bundle.putString("user_id", user_id);
                    main_fg.setArguments(bundle);
                    fTransaction.add(R.id.ly_content, main_fg);
                } else {
                    fTransaction.show(main_fg);
                }
                break;
            case R.id.rb_add:
//                开启新的活动
                Log.d("in add", "in add");
                //用户名和密码传给AddActivity
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_id", user_id);
                startActivityForResult(intent, MainActivity);
                break;
            case R.id.rb_my:
                //if (fg3 == null){
                //用户名传给个人中心
                Bundle bundle2 = new Bundle();
                bundle2.putString("user_name", user_name);
                bundle2.putString("user_id", user_id);
                fg3 = new PersonalFragment();
                fg3.setArguments(bundle2);
                fTransaction.add(R.id.ly_content, fg3);
                //}else{
                //fTransaction.show(fg3);
                //}
                break;
        }
        fTransaction.commit();
    }

    //隐藏所有Fragment
    protected void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (main_fg != null) {
            Log.d("main_fg 存在", "隐藏");
            fragmentTransaction.hide(main_fg);
        }
        if (fg3 != null) fragmentTransaction.hide(fg3);
    }

    /**
     * 设置一个时间变量记录第一次点击的时间
     * 当第二次点击返回键时比较记录的时间和当前的时间
     * 当两者时差在2000ms一类则退出
     */

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从addAcivity中的标题栏和物理返回键返回
        if (resultCode == 0) {
            //底部导航栏改变选中状态
            custom = null;
            rb_channel = (RadioButton) findViewById(R.id.rb_custom);
            rb_channel.setChecked(true);
            fTransaction = fManager.beginTransaction();
            hideAllFragment(fTransaction);
            if (main_fg == null) {
                main_fg = new MainFragment();
                fTransaction.add(R.id.ly_content, main_fg);
            } else {
                fTransaction.show(main_fg);
            }
            fTransaction.commit();
        } else if (resultCode == 1) {
            // 添加的一条新习惯的数据
            user_id = data.getStringExtra("user_id");
            custom_name = data.getStringExtra("custom_name");
            alarm_time = data.getStringExtra("alarm_time");
            target_day = data.getStringExtra("target_day");
            category = data.getStringExtra("category");

            custom = new Custom();
            custom.setTarget_day(target_day);
            custom.setAlarm_time(alarm_time);
            custom.setCategory(category);
            custom.setCustom_name(custom_name);

            Log.i("MainActivity", "回调.................");
            //底部导航栏改变选中状态
            rb_channel = (RadioButton) findViewById(R.id.rb_custom);
            rb_channel.setChecked(true);
            fTransaction = fManager.beginTransaction();//每进行一次fragment的加载都要重新运行一次fManager.beginTransaction()
            hideAllFragment(fTransaction);
            if (main_fg == null) {
                main_fg = new MainFragment();
                // main_fg.setArguments(bundle);这种方法只在fragment创建时有用
                fTransaction.add(R.id.ly_content, main_fg);
            } else {
                fTransaction.show(main_fg);
            }
            fTransaction.commit();
        }

    }

    public void getData(Callback callback) {
        callback.getResult(custom);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    //test set alias

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        JPushInterface.init(getApplicationContext());
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

//                case MSG_SET_TAGS:
//                    Log.d(TAG, "Set tags in handler.");
//                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
//                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            //ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

}
