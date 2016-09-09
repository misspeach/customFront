package com.example.shizhan.customfront.gehui.PersonManagement;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.adapter.CustomAdapter;
import com.example.shizhan.customfront.model.Custom;
import com.example.shizhan.customfront.model.User;
import com.example.shizhan.customfront.util.HttpCallbackListener;
import com.example.shizhan.customfront.util.HttpUtil;
import com.example.shizhan.customfront.util.UserHttpUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gehui on 16/8/1.
 */
public class PersonalFragment extends Fragment {
    public static final String TAG = "MainFragment";
    private AtomicInteger count = new AtomicInteger(0);//是否加static？
    private static final int SHOW_LIST = 1;
    private static final int SHOW_IMAGE = 2;
    private static final int SHOW_USER = 3;  //显示用户信息
    private Context cContext;   //Context是安卓定义好的连接包
    private List<Custom> cData;  //list<E> list.java里的，安卓自带，可直接用
    private User user;  //定义用户
    private HashMap<String, Bitmap> images = new HashMap<>();
    ;
    private CustomAdapter cAdapter = null;
    private ListView userCustom = null;
    private Handler handler = null;
    private TextView title_text;  //定义接收组件，显示用户名
    private TextView textview_sex;  //定义接收组件，显示性别
    private TextView tv_signature;  //定义接收组件，显示签名
    private TextView tv_birthday;  //定义接收组件，显示生日
    private CircleImageView im_headImage; //显示图片

    //userName应该由Activity传给Fragment
    private String userName = "";
    private String userId;
    private static final String baseUrl = "http://192.168.1.100:8080/";//IP地址会变化！！！出现无法访问服务器的情况！！！
    private static String parameter = "";
    private static String parameterinfo = "";  //定义请求什么，自己起的名字

    //OSS init
    private static final String bucketName = "shenyang";
    private static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
    private OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("POTVuwgchOwAcIE9", "PEAub5vvc9FLwT1bZpbemIcydeb17R");
    private OSS oss = new OSSClient(cContext, endpoint, credentialProvider);

    ///////////////////
    private LinearLayout edit;  //定义层，用于Fragment跳转到Activity
    private Map<String, String> category = new HashMap<>();


    /////////////////////
    //    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        cContext=activity;
//        parameter="?userName=" + userName;
//    }
    public void SendUserData(User user) {

        Intent intent = new Intent(getActivity(), SettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);//序列化
        intent.putExtras(bundle);//发送数据
        startActivity(intent);//启动intent

    }

    public void setHeadPortrait() {


        Bitmap bitm = NativeData.openBitmap(user.getImage());
        if (bitm != null)
            im_headImage.setImageBitmap(bitm);

    }

    public void getImageWithAliyun(final String category, String objectKey) {

        // 构造下载文件请求，这是请求图片时用的，可以把图片放在第三方，给图片一个链接，相当于本地
        GetObjectRequest get = new GetObjectRequest(bucketName, objectKey);

        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                count.getAndIncrement();
                // 请求成功
                Log.d("Content-Length", "" + result.getContentLength());
                try {
                    InputStream inputStream = result.getObjectContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    images.put(category, bitmap);
                    if (count.intValue() == cData.size()) {
                        Message message = new Message();   //用Message接收第三方发过来的图片
                        message.what = SHOW_IMAGE;
                        message.obj = images;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
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

    }

    @Override //定义组件的触动事件
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_index, container, false);
        userCustom = (ListView) view.findViewById(R.id.plist_view);

        title_text = (TextView) view.findViewById(R.id.title_text);
        tv_birthday = (TextView) view.findViewById(R.id.tv_birthday);   //定义接收请求的组件
        textview_sex = (TextView) view.findViewById(R.id.textview_sex);
        tv_signature = (TextView) view.findViewById(R.id.tv_signature);
        im_headImage = (CircleImageView) view.findViewById(R.id.headImage);

        //////////////
        edit = (LinearLayout) view.findViewById(R.id.title_edit);
        edit.setOnClickListener(new View.OnClickListener() {// 监听事件,返回上一页面
            @Override
            public void onClick(View arg0) {
                //Intent intent = new Intent();

                Intent intent = new Intent(getActivity(), SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);//序列化
                intent.putExtras(bundle);//发送数据
                startActivity(intent);//启动intent


                // SendUserData( user);//yan传递数据
                // intent.setClass(getActivity(), SettingActivity.class);// 从个人中心的碎片挑战到设置对应的活动
                //  startActivity(intent);

            }
        });

        //////////////
        //保存category
        category.put("健康", "image/health.png");
        category.put("效率", "image/efficiency.png");
        category.put("学习", "image/learn.png");
        category.put("运动", "image/exercise.png");
        category.put("生活", "image/life.png");

        //得到MainActivity中传过来的用户名
        Bundle bundle = getArguments();
        userName = bundle.getString("user_name");
        userId = bundle.getString("user_id");

        cContext = getActivity();
        //TODO
        //parameterinfo = "?uid=" + 1;  //IDEA 在UserController中定义的，名字随便起
        parameterinfo = "?user_name=" + userName; //按用户名请求，请求格式

        parameter = "?userName=" + userName;
        //          test Toolbar

//         getActivity().setSupportActionBar(toolbar);

        //处理子线程发来的消息
        handler = new Handler() {
            public void handleMessage(Message message) {
                if (message.what == SHOW_LIST) {
                    Log.i("message==show_list", "!!!!!!!!!!!!!!!!");
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
                    Log.i("message==show_image", "!!!!!!!!!!!!!!!!");
                    Log.i("show images", String.valueOf(images.size()));
                    Log.i("show list", String.valueOf(cData.size()));
                    cAdapter = new CustomAdapter(cData, images, cContext);
                    userCustom.setAdapter(cAdapter);
                } else if (message.what == SHOW_USER) {   //如果返回的是SHOW_USER
                    Log.i("message==show_user", "!!!!!!!!!!!!!!!!");
                    title_text.setText(user.getUser_name());  //用title_text来获得用户名
                    if (user.getSex() == 1) {   //如果textview_sex获得的性别是1，textview_sex显示女，否则textview_sex显示男
                        textview_sex.setText("女");
                    } else {
                        textview_sex.setText("男");

                    }
                    setHeadPortrait();
                    tv_birthday.setText(user.getBrithday());  //用tv_birthday获得用户的生日
                    tv_signature.setText(user.getSignature());  //用tv_signature获得用户的签名

                }   //这些都是接收组件的id
            }
        };
        /*
        * 向服务器发送查询请求
        *
        * */

        //请求用户信息

        //UserHttpUtil在util中定义，用于发送HTTP请求，HttpCallbackListenner用于回调，用HttpClient发送请求，请求的是getUserInfo
        UserHttpUtil.sendRequestWithHttpClient(baseUrl + "/getUserInfo" + parameterinfo, new UserHttpUtil.HttpCallbackListener() { //请求用户信息，getUserInfo是起的名字，与IDE中UserJpaRespository，UserController保持一致。

            @Override
            public void onFinish(User response) {  //User是model中定义的

                Log.i("get user info", ".................");
                user = response;
                //加入ID
                user.setId(Integer.valueOf(userId));
                Message message = new Message();  //Message方法，JAVA自带
                message.what = SHOW_USER;  //返回的是SHOW_USER
                message.obj = response;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Log.d("get user info", "请求服务器失败！");
            }
        });

        //请求用户习惯

        HttpUtil.sendRequestWithHttpClient(baseUrl + parameter, new HttpCallbackListener() {

            public void onFinish(String response) {
                Log.i("get user customs ", "...................");
                cData = HttpUtil.parseJSONwithGson(response);
                Log.i("cData", String.valueOf(cData.size()));
                Message message = new Message();
                message.what = SHOW_LIST;
                message.obj = response;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Log.d("get user customs", "请求服务器失败！");
            }
        });

        return view;
    }

}
