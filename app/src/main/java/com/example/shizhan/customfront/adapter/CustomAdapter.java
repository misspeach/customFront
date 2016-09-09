package com.example.shizhan.customfront.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.model.Custom;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shizhan on 16/7/25.
 */
public class CustomAdapter extends BaseAdapter {
    private Context cContext;
    private List<Custom> cData;
    private HashMap<String, Bitmap> images;
    private int pos = -1;

    public CustomAdapter(List<Custom> cData, HashMap<String, Bitmap> images, Context cContext) {
        this.cData = cData;
        this.images = images;
        this.cContext = cContext;
    }

    @Override
    public int getCount() {
        return cData.size();
    }

    @Override
    public Custom getItem(int position) {
        //pos=position;
        Custom custom = cData.get(position);
        //cData.remove(position);
        return custom;
    }

    public void updateImages(HashMap<String, Bitmap> images) {
        this.images = images;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("in the AddCustomAdapter", "in getView");
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(cContext).inflate(R.layout.list_item_card, parent, false);
            holder = new ViewHolder();
            holder.cIcon = (ImageView) convertView.findViewById(R.id.custom_icon);
            holder.cName = (TextView) convertView.findViewById(R.id.custom_name);
            holder.cInsist_day = (TextView) convertView.findViewById(R.id.insist_day);
            holder.alarm_time = (TextView) convertView.findViewById(R.id.alarm_time);
            holder.recorded = (ImageView) convertView.findViewById(R.id.recorded);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();//将Holder存储到convertView中,不用每次调findViewById
        }
//        //图标加载
//        if(images.get(cData.get(position).getCategory())==null)
//            holder.cIcon.setImageResource(R.mipmap.photo);
//        else
        holder.cIcon.setImageBitmap(images.get(cData.get(position).getCategory()));
        holder.cName.setText(cData.get(position).getCustom_name());
        holder.cInsist_day.setText("已坚持" + cData.get(position).getInsist_day() + "天");
        //比较过时间后判断是否打卡
        if (cData.get(position).isRecorded() == 1) {
            Log.d(cData.get(position).getCustom_name(), "recorded");
            holder.recorded.setImageResource(R.mipmap.check1);
            holder.recorded.setVisibility(View.VISIBLE);
        } else {
            Log.d(cData.get(position).getCustom_name(), "not recorded............");
            holder.recorded.setVisibility(View.GONE);
        }
        Log.d("position" + position, cData.get(position).getAlarm_time());
        if (cData.get(position).getAlarm_time().equals("不提醒")) {
            Log.d("tixing", cData.get(position).getAlarm_time());
            holder.alarm_time.setText("");
        } else {
            holder.alarm_time.setText(" 提醒 " + cData.get(position).getAlarm_time());
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView cIcon;
        TextView cName;
        TextView cInsist_day;
        TextView alarm_time;
        ImageView recorded;
    }
}

