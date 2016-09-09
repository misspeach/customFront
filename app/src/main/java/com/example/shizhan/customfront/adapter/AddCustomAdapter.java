package com.example.shizhan.customfront.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.model.AddCustom;

import java.util.List;

/**
 * Created by shizhan on 16/7/31.
 */
public class AddCustomAdapter extends BaseAdapter {
    private Context Context;
    private List<AddCustom> addData;
    private String re = "";
    private int pos = -1;

    public AddCustomAdapter(Context context, List<AddCustom> addData) {
//        Log.d("in the addcustomadapter","!!!");
        this.Context = context;
        this.addData = addData;
    }

    public void setText(String re, int pos) {
        this.re = re;
        this.pos = pos;
    }


    @Override
    public int getCount() {
        return addData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.d("in the AddCustomAdapter","in getView");
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(Context).inflate(R.layout.list_item_add, parent, false);
            holder = new ViewHolder();
            holder.Image = (ImageView) convertView.findViewById(R.id.image);
            holder.label = (TextView) convertView.findViewById(R.id.label);
            holder.show_text = (TextView) convertView.findViewById(R.id.show_text);
            holder.enter = (ImageView) convertView.findViewById(R.id.enter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();//将Holder存储到convertView中,不用每次调findViewById
        }
        if (pos == position) {
            holder.show_text.setText(re);
            addData.get(position).setShow_text(re);
        } else
            holder.show_text.setText(addData.get(position).getShow_text());
        holder.Image.setImageResource(addData.get(position).getImageId());
        holder.label.setText(addData.get(position).getLabel());
//        Log.d("label",addData.get(position).getLabel());
        holder.enter.setImageResource(addData.get(position).getEnterId());
        return convertView;
    }

    private class ViewHolder {
        ImageView Image;
        TextView label;
        TextView show_text;
        ImageView enter;
    }
}
