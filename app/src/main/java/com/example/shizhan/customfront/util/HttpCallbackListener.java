package com.example.shizhan.customfront.util;

/**
 * Created by shizhan on 16/7/25.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
