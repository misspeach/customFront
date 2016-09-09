package com.example.shizhan.customfront.CustomManagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.calendar.CalendarCard;
import com.example.shizhan.customfront.calendar.CalendarViewAdapter;
import com.example.shizhan.customfront.calendar.Custom;
import com.example.shizhan.customfront.calendar.CustomDate;
import com.example.shizhan.customfront.calendar.DateUtil;
import com.example.shizhan.customfront.model.RecordDate;
import com.example.shizhan.customfront.util.HttpCallbackListener;
import com.example.shizhan.customfront.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by candy on 2016/8/1.
 */
public class show_custom extends Activity implements CalendarCard.OnCellClickListener {
    //layout_id
    private TextView toolbar_title;//习惯名称
    private TextView data1;//已完成天数
    private TextView data2;//目标天数
    private TextView data3;//目前连续天数
    private TextView data4;//最大连续天数
    private Button btn_share;//
    private Handler handler;
    private static final int SELECT_RECORDS = 1;
    //数据库
    private int CustomId;
    private List<RecordDate> cData = new LinkedList<RecordDate>();//已打卡日期列表
    private static final String baseUrl = "http://192.168.1.107:8080/";//IP地址会变化！！！出现无法访问服务器的情况！！！
    private static String parameter = "";
    private String today;
    private String yesterday;
    private Map<String, String> new_record;
    private Map<String, String> new_alarm;
    //活动传递参数
    private int insistDay;
    private int targetDay;
    private int currentInsistDay;
    private int maxInsistDay;
    private String clock_in;
    private String alarm_time;
    private String insist_day;
    //日历列表
    private ViewPager mViewPager;
    private TextView monthText;
    private int mCurrentIndex = 498;
    private CalendarViewAdapter<CalendarCard> adapter;
    private List<Custom> listDay;
    private CalendarCard[] views;
    private SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
    private LinearLayout indicatorLayout;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE
    }  //滑动方向

    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getSupportActionBar().hide();
        setContentView(R.layout.show_custom);
        handler = new Handler() {
            public void handleMessage(Message message) {
                if (message.what == SELECT_RECORDS) {
                    listDay = new ArrayList<>();
                    for (int i = 0; i < cData.size(); i++) {
                        RecordDate r = cData.get(i);
                        String date = r.getDate();
                        int year = Integer.parseInt(date.substring(0, 4));
                        int month = Integer.parseInt(date.substring(5, 7));
                        int day = Integer.parseInt(date.substring(8, 10));
                        Custom custom = new Custom(year, month, day);
                        listDay.add(custom);
                    }
                    views = new CalendarCard[6];
                    for (int i = 0; i < 6; i++) {
                        views[i] = new CalendarCard(show_custom.this, show_custom.this, listDay);
                    }
                    adapter = new CalendarViewAdapter<>(views);
                    // setViewPager();
                    mViewPager.setAdapter(adapter);
//            initCategoryBarPoint(indicatorLayout);
                    mViewPager.setCurrentItem(598);
//                    System.out.println(mViewPager.getCurrentItem()+"=======222=====");
                    mViewPager.setCurrentItem(597);

                    //更新currentInsistDay
                    Date dNow = new Date();   //当前时间
                    Date dBefore = new Date();
                    Calendar calendar = Calendar.getInstance(); //得到日历
                    calendar.setTime(dNow);//把当前时间赋给日历
                    calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
                    dBefore = calendar.getTime();   //得到前一天的时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    today = sdf.format(dNow);
                    yesterday = sdf.format(dBefore);
                    RecordDate lastrecord = cData.get(cData.size() - 1);
                    if (!(lastrecord.getDate().equals(yesterday)))
                        currentInsistDay = 0;
                }
            }
        };

        //获取MainFragment传来的数据
        Intent intent = getIntent();
        final String custom_name = intent.getStringExtra("custom_name");
        String custom_id = intent.getStringExtra("custom_id");
        Log.i("custom_id",custom_id);
        insist_day = intent.getStringExtra("insist_day");
        String target_day = intent.getStringExtra("target_day");
        String current_insist_day = intent.getStringExtra("current_insist_day");
        String max_insist_day = intent.getStringExtra("max_insist_day");
        clock_in = intent.getStringExtra("isRecorded");
        alarm_time = intent.getStringExtra("alarm_time");
        CustomId = Integer.parseInt(custom_id);
        insistDay = Integer.parseInt(insist_day);
        targetDay = Integer.parseInt(target_day);
        currentInsistDay = Integer.parseInt(current_insist_day);//当前连续天数
        maxInsistDay = Integer.parseInt(max_insist_day);
        // 传给layout
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(custom_name);
        data1 = (TextView) findViewById(R.id.show_insist_day);
        data1.setText(insist_day);
        data2 = (TextView) findViewById(R.id.show_target_day);
        data2.setText(target_day);
        data3 = (TextView) findViewById(R.id.show_current_insist_day);
        data3.setText(current_insist_day);
        data4 = (TextView) findViewById(R.id.show_max_insist_day);
        data4.setText(max_insist_day);
        btn_share = (Button) findViewById(R.id.btn_share);

        //打卡
        Button button_clockin = (Button) findViewById(R.id.clockin);
        if (clock_in.equals("1")) {
            button_clockin.setBackgroundResource(R.mipmap.clockin);
            button_clockin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(show_custom.this, "今天已经打过卡啦！", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            button_clockin.setBackgroundResource(R.mipmap.unclockin);
            button_clockin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (insistDay == targetDay) {
                        Toast.makeText(show_custom.this, "习惯已完成", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ("1".equals(clock_in)) {
                        Toast.makeText(show_custom.this, "今天已经打过卡啦！", Toast.LENGTH_SHORT).show();
                    } else {

                        Button unclockin = (Button) findViewById(R.id.clockin);
                        unclockin.setBackgroundResource(R.mipmap.clockin);
                        insistDay++;
                        currentInsistDay++;
                        if (currentInsistDay > maxInsistDay)
                            maxInsistDay = currentInsistDay;
                        if (insistDay == targetDay)
                            Toast.makeText(show_custom.this, "恭喜你，习惯已养成！", Toast.LENGTH_SHORT).show();//通过Toast输出
                        else
                            Toast.makeText(show_custom.this, "恭喜你，打卡成功！", Toast.LENGTH_SHORT).show();
                        clock_in = "1";
                        data1.setText(String.valueOf(insistDay));
                        data3.setText(String.valueOf(currentInsistDay));
                        data4.setText(String.valueOf(maxInsistDay));

                        //画今天的圈
                        Date dNow = new Date();   //当前时间
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        today = sdf.format(dNow);
                        int year = Integer.parseInt(today.substring(0, 4));
                        int month = Integer.parseInt(today.substring(5, 7));
                        int day = Integer.parseInt(today.substring(8, 10));
                        Custom custom = new Custom(year, month, day);
                        listDay.add(custom);
                        for (int i = 0; i < 6; i++) {
                            views[i] = new CalendarCard(show_custom.this, show_custom.this, listDay);
                        }
                        adapter = new CalendarViewAdapter<>(views);
                        // setViewPager();
                        mViewPager.setAdapter(adapter);
//            initCategoryBarPoint(indicatorLayout);
                        mViewPager.setCurrentItem(598);
                        System.out.println(mViewPager.getCurrentItem() + "======123456====");
                        mViewPager.setCurrentItem(597);

                        // 更新数据库
                        new_record = new HashMap<String, String>();
                        new_record.put("custom_id", String.valueOf(CustomId));
                        new_record.put("date", today);
                        new_record.put("current_insist_day", String.valueOf(currentInsistDay));
                        new_record.put("max_insist_day", String.valueOf(maxInsistDay));
                        new_record.put("insist_day", String.valueOf(insistDay));
                        new_record.put("is_record", clock_in);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtil.postRequest(baseUrl + "clockin", new_record, new HttpCallbackListener() {
                                    @Override
                                    public void onFinish(String response) {
                                        if (response.equals("success")) {
                                            Log.d("....", "success");
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.d("clock in", "请求服务器失败！");
                                    }
                                });
                            }
                        }).start();
                    }
                }
            });
        }
//        返回按钮
        Button button_back = (Button) findViewById(R.id.back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向活动1返回数据
                Intent intent = new Intent();
                intent.putExtra("isRecorded", clock_in);
                intent.putExtra("alarm_time", alarm_time);
                intent.putExtra("insist_day", String.valueOf(insistDay));
                setResult(2, intent);
                finish();
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "changing正在监督我养成" + custom_name + "习惯，目前已经打卡" + insistDay + "天啦！小伙伴们快来一起让changing监督我们养成好的习惯吧！");

                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
            }
        });

//        设置闹钟按钮
        Button button_clock = (Button) findViewById(R.id.clock);
        button_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(show_custom.this, AlarmTimeChooseActivity.class);
                intent.putExtra("custom_name", custom_name);
                startActivityForResult(intent, 4);
            }
        });
//        初始化日历
        mViewPager = (ViewPager) findViewById(R.id.vp_calendar);
        monthText = (TextView) findViewById(R.id.tvCurrentMonth);
        indicatorLayout = (LinearLayout) findViewById(R.id.layout_drop);
        int month = DateUtil.getCurrentMonthNow();
        int year = DateUtil.getCurrentYeatNow();
        CustomDate c = new CustomDate(year, month, 1);
        monthText.setText(showTimeCount(c));
        initData();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //处理AlarmTimeChooseActivity中穿过来的数据
            alarm_time = data.getStringExtra("alarmTime");
            new_alarm = new HashMap<String, String>();
            new_alarm.put("alarm_time", alarm_time);
            new_alarm.put("custom_id", String.valueOf(CustomId));
            new_alarm.put("target_day", String.valueOf(targetDay));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpUtil.postRequest(baseUrl + "update", new_alarm, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            if (response.equals("success")) {
                                Log.d("....", "success");
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("clock in", "请求服务器失败！");
                        }
                    });
                }
            }).start();
        }

    }

    private void initData() {
        listDay = new ArrayList<>();
        for (int i = 0; i < cData.size(); i++) {
            RecordDate r = cData.get(i);
            String date = r.getDate();
            int year = Integer.parseInt(date.substring(0, 4));
//                    System.out.println("year:"+year);
            int month = Integer.parseInt(date.substring(5, 7));
            int day = Integer.parseInt(date.substring(8, 10));
//                    System.out.println("year:"+day);
            Custom custom = new Custom(year, month, day);
            listDay.add(custom);
        }
        views = new CalendarCard[6];
        for (int i = 0; i < 6; i++) {
            views[i] = new CalendarCard(show_custom.this, show_custom.this, listDay);
        }
        adapter = new CalendarViewAdapter<>(views);
        setViewPager();
        CustomDate c = new CustomDate(DateUtil.getCurrentYeatNow(), DateUtil.getCurrentMonthNow(), DateUtil.getCurrentMonthDay());

        parameter = "?CustomId=" + CustomId;
        //发送服务器请求，查询打卡记录
        HttpUtil.sendRequestWithHttpClient(baseUrl + "/record" + parameter, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                cData = HttpUtil.recordsJSONwithGson(response);//把数据库传过来的JSON数据转化为List<Custom>
                System.out.println(cData);
                Message message = new Message();
                message.what = SELECT_RECORDS;
                message.obj = response;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Log.d("user customs", "请求服务器失败！");
            }
        });

    }

    //点击日期显示的toast
    @Override
    public void clickDate(CustomDate date) {
//        Toast.makeText(this,showTimeCountAll(date),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeDate(CustomDate date) {
    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
//            initCategoryBarPoint(indicatorLayout);
        mViewPager.setCurrentItem(mCurrentIndex);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                System.out.println(position + "=========333========");
                measureDirection(position);
                updateCalendarView(position);
//                    setIndicator(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void initCategoryBarPoint(LinearLayout indicatorLayout) {

        indicatorLayout.removeAllViews();

        android.widget.LinearLayout.LayoutParams lp;
        for (int i = 0; i < views.length; i++) {
            View v = new View(this);
            v.setBackgroundResource(R.drawable.point_background_calendar);
            v.setEnabled(false);
            lp = new android.widget.LinearLayout.LayoutParams(dip2px(getApplicationContext(), 12), dip2px(getApplicationContext(), 4));
            lp.leftMargin = dip2px(getApplicationContext(), 3);
            lp.rightMargin = dip2px(getApplicationContext(), 3);
            v.setLayoutParams(lp);
            indicatorLayout.addView(v);
        }

        if (indicatorLayout.getChildCount() > 0)
            indicatorLayout.getChildAt(0).setEnabled(true);

    }

    /**
     * 设置指示器
     *
     * @param selectedPosition 默认指示器位置
     */
    private void setIndicator(int selectedPosition) {
        for (int i = 0; i < views.length; i++) {
            indicatorLayout.getChildAt(i).setEnabled(false);
        }
        if (views.length > selectedPosition)
            indicatorLayout.getChildAt(selectedPosition).setEnabled(true);

    }

    /* 计算方向
   *
    */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    // 更新日历视图
    private void updateCalendarView(int arg0) {
        CustomDate customDate = new CustomDate();
        CalendarCard[] mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            customDate = mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            customDate = mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
        if (customDate != null) {
            monthText.setText(showTimeCount(customDate));
            //进行网络请求

        }


    }

    public String showTimeCount(CustomDate time) {
        String timeCount;
        long minuec = time.month;
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());
        long secc = time.day;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = time.year + "年" + minue + "月";
        return timeCount;
    }
//点击日期显示的toast
//    public String showTimeCountAll(CustomDate time) {
//        String timeCount;
//        long minuec = time.month;
//        String minue = "0" + minuec;
//        minue = minue.substring(minue.length() - 2, minue.length());
//        long secc = time.day;
//        String sec = "0" + secc;
//        sec = sec.substring(sec.length() - 2, sec.length());
//        timeCount = time.year + minue + sec;
//        return timeCount;
//    }

    public int dip2px(Context ctx, float dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        //dp = px/density
        int px = (int) (dp * density + 0.5f);
        return px;
    }

    //重写返回键
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isRecorded", clock_in);
        intent.putExtra("alarm_time", alarm_time);
        intent.putExtra("insist_day", String.valueOf(insistDay));
        setResult(2, intent);
        finish();
    }
}

