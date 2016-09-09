package com.example.shizhan.customfront.CustomManagement;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.example.shizhan.customfront.MainActivity;
import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.adapter.CustomAdapter;
import com.example.shizhan.customfront.model.Custom;
import com.example.shizhan.customfront.util.Callback;
import com.example.shizhan.customfront.util.HttpCallbackListener;
import com.example.shizhan.customfront.util.HttpUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shizhan on 16/7/29.
 */
public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";

    private AtomicInteger count = new AtomicInteger(0);//是否加static？
    private static final int SHOW_LIST = 1;
    private static final int SHOW_IMAGE = 2;
    private Context cContext;
    private List<Custom> cData = new LinkedList<Custom>();
    private HashMap<String, Bitmap> images = new HashMap<>();
    ;
    private CustomAdapter cAdapter = null;
    private ListView userCustom = null;
    private Handler handler = null;

    //test
    private Map<String, String> category = new HashMap<>();

    //userName应该由Activity传给Fragment
    // private String userName = "shizhan";
    private String userName;
    private int pos_longClick = -1;
    private int pos_shortClick = -1;
    private String new_target_day;
    private String new_alarm_time;

    //通过查询得到userID,share
//    private Long userId;
    private String user_id;
    private static final String baseUrl = "http://192.168.1.100:8080/";//IP地址会变化！！！出现无法访问服务器的情况！！！
    private static String parameter = "";

    //OSS init
    private static final String bucketName = "shenyang";
    private static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
    private OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("POTVuwgchOwAcIE9", "PEAub5vvc9FLwT1bZpbemIcydeb17R");
    private OSS oss = new OSSClient(cContext, endpoint, credentialProvider);

    //test
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private View view_custom;

    //判断listview首次加载还是更新
    private int flag = 0;

    /*
    * onCreateView只在第一次创建碎片的时候调用
    * */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("MainFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fg_custom, container, false);
        userCustom = (ListView) view.findViewById(R.id.list_view);
        cContext = getActivity();

        //保存category
        category.put("健康", "image/health.png");
        category.put("效率", "image/efficiency.png");
        category.put("学习", "image/learn.png");
        category.put("运动", "image/exercise.png");
        category.put("生活", "image/life.png");

        //保存MainActivity中的user_name,user_id
        Bundle bundle = getArguments();
//        if(bundle==null)
//            System.out.println("*************");
//        else
//            System.out.println("(((((((((((((");
        userName = bundle.getString("user_name");

        Log.i("MainFragment", userName);
//        System.out.println(userName);
        user_id = bundle.getString("user_id");
        Log.i("MainFragment", user_id);
//        Log.i("MainFragment",bundle.getString("user_id"));

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.custom_toolbar);
        //ToolBar默认显示项目名，这里不显示ToolBar默认的
        toolbar.setTitle("");
        userCustom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                pos_shortClick = position;
                ////////////////点击某个习惯进入打卡页面,记得改///////////////////////////////
                Intent intent = new Intent(cContext, show_custom.class);
                //得到点击的习惯ID
                String custom_id = String.valueOf(cData.get(position).getCustom_Id());
                String custom_name = cData.get(position).getCustom_name();
                String current_insist_day = cData.get(position).getCurrent_insist_day();//当前连续天数
                String max_insist_day = cData.get(position).getMax_insist_day();//最大连续天数
                String insist_day = cData.get(position).getInsist_day();//已坚持天数
                String target_day = cData.get(position).getTarget_day();//目标天数
                String alarm_time = cData.get(position).getAlarm_time();
                String isRecorded = String.valueOf(cData.get(position).isRecorded());
                //HttpUtil.sendRequestWithHttpClient(baseUrl+);
                //test
                Log.i("custom id to showcustom",custom_id);

                intent.putExtra("custom_id", custom_id);
                intent.putExtra("custom_name", custom_name);
                intent.putExtra("current_insist_day", current_insist_day);
                intent.putExtra("max_insist_day", max_insist_day);
                intent.putExtra("insist_day", insist_day);
                intent.putExtra("target_day", target_day);
                intent.putExtra("alarm_time", alarm_time);
                intent.putExtra("isRecorded", isRecorded);
                startActivityForResult(intent, 2);
            }
        });
        userCustom.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                pos_longClick = position;
                //初始化Builder
                builder = new AlertDialog.Builder(cContext);

                //加载自定义的那个View,同时设置下
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                view_custom = inflater.inflate(R.layout.view_dialog_custom, null, false);
                builder.setView(view_custom);
                builder.setCancelable(false);
                alert = builder.create();
                alert.show();
                view_custom.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                view_custom.findViewById(R.id.btn_setting).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //默认值
                        new_target_day = cData.get(position).getTarget_day();
                        new_alarm_time = cData.get(position).getAlarm_time();

                        Intent intent = new Intent(cContext, CustomSettingActivity.class);
                        //intent.putExtra("postiton",position);
                        intent.putExtra("day", cData.get(position).getTarget_day());
                        intent.putExtra("time", cData.get(position).getAlarm_time());
                        intent.putExtra("name", cData.get(position).getCustom_name());
                        startActivityForResult(intent, 1);
                        alert.dismiss();
                    }
                });

                view_custom.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Custom custom = cAdapter.getItem(position);
                        Long id = custom.getCustom_Id();
                        Map<String, String> dataMap = new HashMap<String, String>();
                        dataMap.put("custom_id", String.valueOf(id));
                        String name = custom.getCustom_name();
                        Log.i("remove " + String.valueOf(id), name);
                        Log.i("category", cAdapter.getItem(position).getCategory());
                        cData.remove(position);
                        images.remove(position);
                        cAdapter.notifyDataSetChanged();
                        alert.dismiss();
                        Toast.makeText(cContext, "该习惯已删除！", Toast.LENGTH_SHORT).show();
                        //删除数据库中的习惯记录
                        parameter = "delete";
                        HttpUtil.postRequest(baseUrl + parameter, dataMap, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                if (response.equals("delete success")) {
                                    Log.d("delete success", "*********");
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d("user customs", "请求服务器失败！");
                            }
                        });
                    }
                });
                return true;//改为true
            }
        });

        //处理子线程发来的消息
        handler = new Handler() {
            public void handleMessage(Message message) {
                if (message.what == SHOW_LIST) {
                    for (int i = 0; i < cData.size(); i++) {
                        boolean tmp = false;
                        for (int j = 0; j < images.size(); j++) {
                            if (images.containsKey(cData.get(i).getCategory())) {
                                tmp = true;
                            }
                        }
                        if (tmp == false) {
                            getImageWithAliyun(cData.get(i).getCategory(), category.get(cData.get(i).getCategory()));
                        }
                    }
                } else if (message.what == SHOW_IMAGE) {
                    Log.i("images", String.valueOf(images.size()));
                    Log.i("cData", String.valueOf(cData.size()));
                    cAdapter = new CustomAdapter(cData, images, cContext);
                    userCustom.setAdapter(cAdapter);
                }
            }
        };
        /*
        * 向服务器发送查询请求获取用户习惯
        *
        * */
        parameter = "?userName=" + userName;
        HttpUtil.sendRequestWithHttpClient(baseUrl + parameter, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (!response.equals("[]")) {

                    cData = HttpUtil.parseJSONwithGson(response);

                    Log.d("user customs ", "success!");
                    Message message = new Message();
                    message.what = SHOW_LIST;
                    message.obj = response;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("user customs", "请求服务器失败！");
            }
        });
        return view;
    }

    //收到customsetting返回的修改数据，此时活动和碎片只是重启没有重建！
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            int isRecorded = Integer.valueOf(data.getStringExtra("isRecorded"));
            String alarm_time = data.getStringExtra("alarm_time");
            String insist_day = data.getStringExtra("insist_day");
            cData.get(pos_shortClick).setRecorded(isRecorded);
            cData.get(pos_shortClick).setAlarm_time(alarm_time);
            cData.get(pos_shortClick).setInsist_day(insist_day);
            Log.i("new alarm_time", alarm_time);
            Log.i("new insist_day", insist_day);
            Log.i("new recorded", String.valueOf(isRecorded));
            cAdapter.notifyDataSetChanged();
        }
        else if(resultCode==3){
            Log.i("main Fragment", data.getStringExtra("new_target_day"));
            Log.i("main Fragment", data.getStringExtra("new_alarm_time"));
            new_target_day = data.getStringExtra("new_target_day");
            new_alarm_time = data.getStringExtra("new_alarm_time");
            //更改了数据
            if (!new_alarm_time.equals("") && !new_target_day.equals("")) {
                cData.get(pos_longClick).setAlarm_time(new_alarm_time);
                cData.get(pos_longClick).setTarget_day(new_target_day);
                cAdapter.notifyDataSetChanged();
                Toast.makeText(cContext, "该习惯已经重新设置！", Toast.LENGTH_SHORT).show();
                // 更新数据库中的习惯记录
                Map<String, String> map = new HashMap<String, String>();
                Long custom_id = cData.get(pos_longClick).getCustom_Id();
                map.put("custom_id", String.valueOf(custom_id));
                map.put("alarm_time", new_alarm_time);
                map.put("target_day", new_target_day);
                //重新设置习惯后更新数据库
                parameter = "update";
                HttpUtil.postRequest(baseUrl + parameter, map, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        if (response.equals("update success")) {
                            Log.d("update success", "*********");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("user customs", "请求服务器失败！");
                    }
                });
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        if (flag != 0) {//更新列表时才执行，初次登录时不调用
            ((MainActivity) getActivity()).getData(new Callback() {
                @Override
                public void getResult(final Custom custom) {
//                    Log.i("getResult","custom");
//                    System.out.println("getResult");
                    handler = new Handler() {
                        public void handleMessage(Message message) {
                            Log.i("images", String.valueOf(images.size()));
                            Log.i("cData", String.valueOf(cData.size()));
                            if (message.what == SHOW_IMAGE) {
                                if (cAdapter == null) {
                                    cAdapter = new CustomAdapter(cData, images, cContext);
                                    userCustom.setAdapter(cAdapter);
                                } else {
                                    cAdapter.updateImages(images);
                                    Log.i("images", String.valueOf(images.size()));
                                    Log.i("cData", String.valueOf(cData.size()));
                                    cAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    };
                    if (custom != null) {
                        // Log.i("*****","*******");
                        Log.i("custom_name", custom.getCustom_name());
                        Log.i("category", custom.getCategory());
                        custom.setInsist_day("0");
                        if (cAdapter == null) {
                            //Log.i("onstart","starting");
                            cAdapter = new CustomAdapter(cData, images, cContext);
                            userCustom.setAdapter(cAdapter);
                        }
                        Custom custom1;
                        boolean inserted = false;
                        //Log.i("cData",String.valueOf(cData.size()));
                        for (int i = 0; i < cData.size(); i++) {
                            custom1 = cData.get(i);
//                            Log.i("inserted ",custom1.getCustom_name());
//                            Log.i(custom1.getCustom_name(),custom.getCustom_name());
                            if ((custom1.getCustom_name()).equals(custom.getCustom_name())) {
                                inserted = true;
                            }
                        }
                        if (inserted == false) {
                            cData.add(custom);
                            if (!images.containsKey(custom.getCategory())) {
                                getImageWithAliyun(custom.getCategory(), category.get(custom.getCategory()));
                            }
                            cAdapter.notifyDataSetChanged();
                            //网络请求分类图片并在数据库中插入
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("user_id", user_id);
                            map.put("custom_name", custom.getCustom_name());
                            map.put("alarm_time", custom.getAlarm_time());
                            map.put("target_day", custom.getTarget_day());
                            map.put("category", custom.getCategory());
                            //post请求插入一个习惯
                            HttpUtil.postRequest(baseUrl, map, new HttpCallbackListener() {
                                @Override
                                public void onFinish(String response) {
                                    //Log.i("image url", response);
                                    Log.i("custom_id",response);

                                    cData.get(cData.size()-1).setCustom_Id(Long.valueOf(response));
                                    cData.get(cData.size()-1).setCurrent_insist_day("0");
                                    cData.get(cData.size()-1).setMax_insist_day("0");
                                    boolean exist = false;
                                    for (int i = 0; i < images.size(); i++) {
                                        if (images.containsKey(custom.getCategory())) {
                                            exist = true;
                                        }
                                    }
                                    if (exist == false) {
                                        getImageWithAliyun(custom.getCategory(), category.get(custom.getCategory()));
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.d("MainFragment", "请求服务器失败！");
                                }
                            });
                        }
                    }
                }
            });
        }
        flag = 1;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
    }

    public void getImageWithAliyun(final String category, String objectKey) {

        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(bucketName, objectKey);

        Log.i("bucketName", bucketName);
        Log.i("objectKey", objectKey);

        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {

                // 请求成功
                Log.i("Content-Length", "" + result.getContentLength());
                try {
                    InputStream inputStream = result.getObjectContent();

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    images.put(category, bitmap);
                    count.getAndIncrement();
                    if (count.intValue() == images.size()) {
                        Log.i("enter ", "enter getAliyun");
                        Message message = new Message();
                        message.what = SHOW_IMAGE;
                        message.obj = images;
                        handler.sendMessage(message);
                    }
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    Log.i("本地异常", "fail");
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
//        task.isCompleted();
//        task.waitUntilFinished();
    }
}
