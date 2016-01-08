package com.xiaogang.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.model.ItemGrid;

import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 * ºáÏß¹ö¶¯µÄgridView
 */
public class ItemHoriGridViewAdapter extends BaseAdapter {
    Context context;
    List<ItemGrid> list;
    public ItemHoriGridViewAdapter(Context _context, List<ItemGrid> _list) {
        this.list = _list;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_hori_gridview, null);
        ImageView tvCity = (ImageView) convertView.findViewById(R.id.pic);
        TextView tvCode = (TextView) convertView.findViewById(R.id.title);
        ItemGrid city = list.get(position);
        tvCode.setText(city.getTitle());
        tvCity.setImageDrawable(context.getResources().getDrawable(city.getPic()));
        return convertView;
    }
}
