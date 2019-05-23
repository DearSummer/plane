package com.billy.plane.Adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.billy.plane.Entity.Item_settting;
import com.billy.plane.R;

import java.util.List;

public class SettingAdapter extends ArrayAdapter<Item_settting> {
    private int resourceId;

    public SettingAdapter(@NonNull Context context, int resource, List<Item_settting> objects) {
        super(context, resource,objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item_settting item = getItem(position);
        View view ;
        ViewHolder viewHolder;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.img);
            viewHolder.textView = (TextView) view.findViewById(R.id.text);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.imageView.setImageResource(item.getItemImg());
        viewHolder.textView.setText(item.getText());
        return view;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
