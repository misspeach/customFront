package com.example.shizhan.customfront.util;

import android.util.Log;

import com.example.shizhan.customfront.model.Custom;
import com.example.shizhan.customfront.model.RecordDate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by shizhan on 16/7/25.
 */
public class HttpUtil {

    public static HttpClient httpClient = new DefaultHttpClient();

    public static void sendRequestWithHttpClient(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpGet httpGet = new HttpGet(address);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        // 请求和响应都成功
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        //Gson解析服务器发过来的数据
                        Log.d("data from the server：", response);

                        if (listener != null) {
                            //回调onFinish()方法
                            listener.onFinish(response);
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

    public static void postRequest(final String address, final Map<String, String> rawParams, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建HttpPost对象
                    HttpPost post = new HttpPost(address);
                    // 对传递的请求参数进行封装
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    if (rawParams != null && !rawParams.isEmpty()) {
                        for (String key : rawParams.keySet()) {
                            // 封装请求参数
                            params.add(new BasicNameValuePair(key, rawParams.get(key)));
                        }
                    }
                    Log.d("parameter size", String.valueOf(params.size()));
                    // 设置请求参数
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    // 发送POST请求
                    HttpResponse httpResponse = httpClient.execute(post);

                    // 如果服务器成功地返回响应
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        // 获取服务器响应字符串
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        Log.d("data from the server：", result);

                        if (listener != null) {
                            //回调onFinish()方法
                            Log.d("enter onFinish", result);
                            listener.onFinish(result);
                        }
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        Log.d("HttpClient-post", "in the catch");
                        listener.onError(e);
                    } else {
                        Log.d("HttpClient_post", "listener null");
                    }
                    e.printStackTrace();
                }
            }
        }).start();

    }
    /*
    * 将JSON格式数据转化为普通数据
    * */

    public static List<RecordDate> recordsJSONwithGson(String response) {
        Gson gson = new Gson();
        List<RecordDate> dateList = gson.fromJson(response, new TypeToken<List<RecordDate>>() {
        }.getType());
        for (RecordDate recorddate : dateList) {
            Log.d("record_date", recorddate.getDate());
        }
        return dateList;
    }


    public static List<Custom> parseJSONwithGson(String response) {
        Gson gson = new Gson();
        List<Custom> customList = gson.fromJson(response, new TypeToken<List<Custom>>() {
        }.getType());
        for (Custom custom : customList) {
            Log.d("custom_name", custom.getCustom_name());
            Log.d("alarm_time", custom.getAlarm_time());
            Log.d("insist_day", custom.getInsist_day());
            //Log.d("image_url", custom.getImage_url());
            Log.d("category", custom.getCategory());
            Log.d("target_day", custom.getTarget_day());
            Log.d("max_insist_day", custom.getMax_insist_day());
            Log.d("current_insist_day", custom.getCurrent_insist_day());
            Log.d("custom_Id", String.valueOf(custom.getCustom_Id()));
            Log.d("user_Id", String.valueOf(custom.getUser_Id()));
            Log.d("isRecorded", String.valueOf(custom.isRecorded()));
        }
        return customList;
    }

    public static Map convertToMapWithJSON(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);

        Map<String, String> result = new HashMap<String, String>();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;

        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            Log.d(key, value);
            result.put(key, value);

        }
        return result;
    }

}

