package com.example.shizhan.customfront.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/**
 * @param
 * @return
 */
public class HttpManager {

    private RequestQueue mQueue;
    public static HttpManager mHttpManager;

//	public static HttpManager getInstance(Context context) {
//
//		if (mHttpManager == null) {
//			mHttpManager = new HttpManager(context);
//		}
//
//		return mHttpManager;
//	}

    public HttpManager(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public void getString(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        try {

            Log.v("HTTP", url);

            StringRequest request = new StringRequest(url, listener, errorListener);

            mQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void postString(String url, final Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener) {

//		Log.v("HTTP",url+" : "+ params.toString());

        StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=GBK");

                return headers;
            }
        };


        mQueue.add(request);

    }

}
