package com.example.shizhan.customfront.util;

import android.util.Log;

import com.example.shizhan.customfront.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by gehui on 16/8/2.
 */
public class UserHttpUtil {
    public interface HttpCallbackListener {  //回调方法，在PersonalFragment中发送请求时调用
        void onFinish(User response);

        void onError(Exception e);
    }

    public static void sendRequestWithHttpClient(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() { //new一个线程来发送请求
            @Override
            public void run() { //继承父类的run方法
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(address);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        // 请求和响应都成功
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        //Gson解析服务器发过来的数据
                        Log.d("data from the server：", response);

                        //Log.d("show response",response);
//                        Message message = new Message();
//                        message.what = SHOW_RESPONSE;
//                        // 将服务器返回的结果存放到Message中
//                        message.obj = response.toString();
//                        handler.sendMessage(message);
                        if (listener != null) {
                            //回调onFinish()方法
                            listener.onFinish(parseJSONwithGson(response));
                        }
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        Log.d("HttpClient", "in the catch");
                        listener.onError(e);
                    } else {
                        Log.d("HttpClient", "listener null");
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static User parseJSONwithGson(String response) {
        Gson gson = new Gson();
        System.out.println(response);
        User user = gson.fromJson(response, new TypeToken<User>() {
        }.getType());

        return user;
    }

}

