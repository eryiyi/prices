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
import com.xiaogang.myapp.model.SearchGoods;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/5
 * Time: 16:17
 * 商品搜索
 */
public class ItemSearchGoodsByTypeAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<SearchGoods> lists;
    private Context mContext;

    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    public ItemSearchGoodsByTypeAdapter(List<SearchGoods> lists, Context mContext) {
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
    public View getView(final  int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_goods, null);
            holder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            holder.item_prices = (TextView) convertView.findViewById(R.id.item_prices);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SearchGoods favour = lists.get(position);
        if (favour != null) {
            imageLoader.displayImage(favour.getThumbnail(), holder.item_img,
                    PricesApplication.options, animateFirstListener);
            holder.item_title.setText(favour.getName());
            holder.item_prices.setText(mContext.getResources().getString(R.string.prices_type) + favour.getPrice_sale());
        }

        return convertView;
    }
    class ViewHolder{
        ImageView item_img;
        TextView item_prices;
        TextView item_title;
    }
}

