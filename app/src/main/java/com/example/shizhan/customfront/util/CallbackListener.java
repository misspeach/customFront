package com.example.shizhan.customfront.util;

/**
 * Created by Administrator on 2016/8/3.
 */
public interface CallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
