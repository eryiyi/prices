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
import com.xiaogang.myapp.model.News;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/5
 * Time: 16:17
 * 最新情报
 */
public class ItemNewsAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<News> lists;
    private Context mContext;
    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    public ItemNewsAdapter(List<News> lists, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news_xml, null);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);
            holder.news_num = (TextView) convertView.findViewById(R.id.news_num);
            holder.bottom_one = (TextView) convertView.findViewById(R.id.bottom_one);
            holder.bottom_two = (TextView) convertView.findViewById(R.id.bottom_two);
            holder.bottom_three = (TextView) convertView.findViewById(R.id.bottom_three);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final News favour = lists.get(position);
        if (favour != null) {
            imageLoader.displayImage(favour.getThumbnail(), holder.item_pic,
                    PricesApplication.options, animateFirstListener);
            holder.item_title.setText(favour.getTitle());
            int tmpposit = position+1;
            holder.news_num.setText("NO."+tmpposit);
        }

        //点击事件
        holder.bottom_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, null);
            }
        });

        return convertView;
    }
    class ViewHolder{
        ImageView item_pic;
        TextView news_num;
        TextView item_title;
        TextView item_cont;
        TextView bottom_one;
        TextView bottom_two;
        TextView bottom_three;
    }
}

