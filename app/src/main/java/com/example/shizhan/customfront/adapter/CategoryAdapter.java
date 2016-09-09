package com.example.shizhan.customfront.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.model.Category;

import java.util.List;

/**
 * Created by shizhan on 16/8/1.
 */
public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categories;
    private int selectPos = 0;
    private String show_category = "";

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearPosition(int pos) {
        selectPos = pos;
    }

    public String getCategory(int pos) {
        return show_category;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.d("in the AddCustomAdapter","in getView");
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false);
            holder = new ViewHolder();
            holder.cIcon = (ImageView) convertView.findViewById(R.id.category_image);
            holder.cName = (TextView) convertView.findViewById(R.id.category_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();//将Holder存储到convertView中,不用每次调findViewById
        }
        if (selectPos == position) {
            holder.cIcon.setImageResource(R.mipmap.choose_pressed);
            show_category = categories.get(position).getCategoryName();
//            Log.i("getView",show_category);
            //getCategory(position);
        } else
            holder.cIcon.setImageResource(R.mipmap.choose);
        holder.cName.setText(categories.get(position).getCategoryName());
//        Log.d("position"+position,cData.get(position).getAlarm_time());

        return convertView;
    }

    private class ViewHolder {
        ImageView cIcon;//图标
        TextView cName;//分类
    }
}
