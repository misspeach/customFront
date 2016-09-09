package com.example.shizhan.customfront.gehui.PersonManagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.shizhan.customfront.model.User;
import com.example.shizhan.customfront.util.HttpCallbackListener;
import com.example.shizhan.customfront.util.HttpUtil;
import com.example.shizhan.customfront.util.UserHttpUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by you on 2016/8/9.
 */


public class NativeData {
    User user = null; //主要就是维护这张表


    public Handler handler = null;
    public static final int SHOW_USER = 3;  //显示用户信息
    public static final int SHOW_CHANGE = 4;  //显示用户信息


    private static final String baseUrl = "http://192.168.1.100:8080/";//IP地址会变化！！！出现无法访问服务器的情况！！！

    public NativeData(User user) {
        this.user = user;
    }

    public NativeData() {
        getInfomationFromInternet();
    }

    public boolean isNull() {
        if (user == null)
            return true;
        else
            return false;
    }

    public void getInfomationFromInternet() {
        String parameterinfo = "";
        parameterinfo = "?uid=" + user.getId(); //ID没有设置
        UserHttpUtil.sendRequestWithHttpClient(baseUrl + "/getUserInfo" + parameterinfo, new UserHttpUtil.HttpCallbackListener() { //请求用户信息，getUserInfo是起的名字，与IDE中UserJpaRespository，UserController保持一致。
            @Override
            public void onFinish(User response) {  //User是model中定义的
                user = response;
                Message message = new Message();  //Message方法，JAVA自带
                message.what = SHOW_USER;  //返回的是SHOW_USER
                message.obj = response;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Log.d("user customs", "请求服务器失败！");
            }
        });

    }


    private int flag_ret = 0; //用于判断是否更新成功！
    private int wait = 0;
    private int cout = 0;

    protected boolean changeUserInfomation() {

        if (isNull()) {
            return false;
        }


        flag_ret = 0;
        Map<String, String> new_usr; //用于修改
        new_usr = new HashMap<String, String>();
        new_usr.put("uid", String.valueOf(user.getId()));    //ID没有设置，应该可以从本地数据中获得
        new_usr.put("user_name", user.getUser_name());
        new_usr.put("password", user.getPassword());
        new_usr.put("email", user.getEmail());
        new_usr.put("tel", user.getTel());
        new_usr.put("sex", String.valueOf(user.getSex()));
        new_usr.put("birthday", user.getBrithday());
        new_usr.put("signature", user.getSignature());
        new_usr.put("image", user.getImage());

        HttpUtil.postRequest(baseUrl + "/changeUserInfo", new_usr, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (response.equals("update success")) {
//                    Log.d("....","update success");
//                    Message message=new Message();
//                    message.what=SHOW_CHANGE;
//                    handler.sendMessage(message);
                    flag_ret = 1;

                } else {
                    flag_ret = 2;
                }
            }

            @Override
            public void onError(Exception e) {

                Log.d("AddActivity", "请求服务器失败！"

                );
            }
        });

        wait = 0;
        cout = 0;
        final Timer mTimer = new Timer();
        TimerTask mTask = new TimerTask() {
            public void run() {
                // if(cout==1){
                wait = 1;
                mTimer.cancel();
                // }

                cout++;
            }
        };
        mTimer.schedule(mTask, 5000, 5000);//5s后执行，3s一次


        while (flag_ret == 0) {
            if (wait == 1)
                break;
        }


        if (flag_ret == 1)
            return true;
        if (flag_ret == 2)
            return false;
        else
            return false;
    }


    protected boolean getInfoFromWidget(int uid,
                                        String user_name,
                                        // String password,
                                        String email,
                                        String tel,
                                        int sex,
                                        String birthday,
                                        String signature,
                                        String image) {//从控件中获取要更改的信息

        if (isNull()) {
            return false;
        }
        if (uid != -1)
            user.setId(uid);

        if (user_name != null)
            user.setUser_name(user_name);

        //  if(password!=null)
        // user.setPassword(password);

        if (email != null)
            user.setEmail(email);

        if (tel != null)
            user.setTel(tel);

        if (sex != -1)
            user.setSex(sex);

        if (birthday != null)
            user.setBrithday(birthday);

        if (signature != null)
            user.setSignature(signature);

        if (image != null)
            user.setImage(image);

        return true;
//        new_usr=new HashMap<String, String>();
//        new_usr.put("uid","1");    //ID没有设置，应该可以从本地数据中获得
//        new_usr.put("user_name",text_edit_nickname.toString());
//        new_usr.put("password","sadfasdf");
//        new_usr.put("email","@12321312");
//        new_usr.put("tel","123123");
//
//        if(radio_radio_woman.isChecked()){
//            new_usr.put("sex","1");
//        }else{
//            new_usr.put("sex","0");
//        }
//
//        new_usr.put("birthday","19891017");
//        // new_usr.put("signature",user.getSignature());
//        new_usr.put("image","aaaaaa");
    }

    /**
     * 保存方法
     */
    public static String saveBitmap(Bitmap bm, String fileName) {

        String ALBUM_PATH
                = Environment.getExternalStorageDirectory() + "/CustomFront/";

        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ALBUM_PATH + fileName;
    }


    public static Bitmap openBitmap(String file) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(file, op);
    }


}
