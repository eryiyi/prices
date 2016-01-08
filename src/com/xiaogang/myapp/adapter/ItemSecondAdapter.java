package com.xiaogang.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.myapp.PricesApplication;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.model.SecondGoods;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/5
 * Time: 16:17
 * 二手
 */
public class ItemSecondAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<SecondGoods> lists;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    public ItemSecondAdapter(List<SecondGoods> lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return lists.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_second_xml, null);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_rmb = (TextView) convertView.findViewById(R.id.item_rmb);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SecondGoods favour = lists.get(position);
        if (favour != null) {
            imageLoader.displayImage(favour.getThumbnail(), holder.item_pic, PricesApplication.options, animateFirstListener);
            holder.item_title.setText(favour.getName());
            holder.item_rmb.setText("RMB:"+mContext.getResources().getString(R.string.prices_type)+favour.getPrice());
        }
        return convertView;
    }
    class ViewHolder{
        ImageView item_pic;
        TextView item_title;
        TextView item_rmb;
    }
}

