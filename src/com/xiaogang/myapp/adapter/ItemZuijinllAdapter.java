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
import com.xiaogang.myapp.db.LiulanSmart;
import com.xiaogang.myapp.model.Deng;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/5
 * Time: 16:17
 * 最近浏览
 */
public class ItemZuijinllAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<LiulanSmart> lists;
    private Context mContext;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    public ItemZuijinllAdapter(List<LiulanSmart> lists, Context mContext) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_zjll_xml, null);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_rmb = (TextView) convertView.findViewById(R.id.item_rmb);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LiulanSmart favour = lists.get(position);
        if (favour != null) {
            imageLoader.displayImage(favour.getGoods_cover(), holder.item_pic, PricesApplication.options, animateFirstListener);
            holder.item_title.setText(favour.getGoods_name());
            holder.item_rmb.setText("RMB:￥"+favour.getSell_price());
        }
        return convertView;
    }
    class ViewHolder{
        ImageView item_pic;
        TextView item_title;
        TextView item_rmb;
    }
}

