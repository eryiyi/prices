package com.xiaogang.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.model.GoodDetailSales;
import com.xiaogang.myapp.model.GoodDetailSalesBaojia;
import com.xiaogang.myapp.model.GoodDetailSalesBaojiaSa;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/5
 * Time: 16:17
 * 商品
 */
public class ItemGoodsBaojiaAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<GoodDetailSalesBaojia> lists;
    private Context mContext;
    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    public ItemGoodsBaojiaAdapter(List<GoodDetailSalesBaojia> lists, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_xml, null);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.news_num = (TextView) convertView.findViewById(R.id.news_num);
            holder.location_title = (TextView) convertView.findViewById(R.id.location_title);
            holder.tel_title = (TextView) convertView.findViewById(R.id.tel_title);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.rating_bar = (RatingBar) convertView.findViewById(R.id.rating_bar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodDetailSalesBaojia favour = lists.get(position);
        if (favour != null) {
            int tmpposit = position+1;
            holder.news_num.setText("NO."+tmpposit);
            GoodDetailSalesBaojiaSa goodDetailSalesBaojiaSa = favour.getSa();
            holder.money.setText(favour.getHh() ==null?"":favour.getHh());
            if(goodDetailSalesBaojiaSa != null){
                holder.item_title.setText(goodDetailSalesBaojiaSa.getName()==null?"":goodDetailSalesBaojiaSa.getName());
                holder.location_title.setText(goodDetailSalesBaojiaSa.getAddress()==null?"":goodDetailSalesBaojiaSa.getAddress());
                holder.tel_title.setText(goodDetailSalesBaojiaSa.getMobile()==null?"":goodDetailSalesBaojiaSa.getMobile());
                holder.rating_bar.setRating(Float.valueOf(goodDetailSalesBaojiaSa.getLevel()==null?"":goodDetailSalesBaojiaSa.getLevel()));
            }

        }

        //点击事件
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, 1);
            }
        });

        return convertView;
    }
    class ViewHolder{
        TextView news_num;
        TextView item_title;
        ImageView pic;
        TextView location_title;
        TextView money;
        TextView tel_title;
        RatingBar rating_bar;
    }
}

