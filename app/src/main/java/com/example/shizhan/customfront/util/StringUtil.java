package com.example.shizhan.customfront.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/10.
 */
public class StringUtil {
    public static boolean isEmpty(Context c, String str) {
        if (null != str && str.trim().length() > 0) {
            return false;
        } else {
            Toast.makeText(c, "您输入有误", Toast.LENGTH_LONG).show();
            return true;
        }
    }

}
