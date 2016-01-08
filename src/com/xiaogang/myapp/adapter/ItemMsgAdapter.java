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
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.model.Minemsg;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/5
 * Time: 16:17
 * 消息
 */
public class ItemMsgAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Minemsg> lists;
    private Context mContext;
    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    public ItemMsgAdapter(List<Minemsg> lists, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_msg, null);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.item_cont = (TextView) convertView.findViewById(R.id.item_cont);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Minemsg favour = lists.get(position);
        if (favour != null) {
//            imageLoader.displayImage(favour.getEmpCover(), holder.andme_item_cover, UniversityApplication.txOptions, animateFirstListener);
            holder.item_pic.setImageDrawable(mContext.getResources().getDrawable(favour.getPic()));
            holder.item_title.setText(favour.getTitle());
            holder.item_dateline.setText(favour.getDateline());
            holder.item_cont.setText(favour.getCont());

        }
//        //点击事件
//        holder.item_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickContentItemListener.onClickContentItem(position, 1, null);
//            }
//        });
//        holder.item_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickContentItemListener.onClickContentItem(position, 1, null);
//            }
//        });

        return convertView;
    }
    class ViewHolder{
        ImageView item_pic;
        TextView item_title;
        TextView item_dateline;
        TextView item_cont;
    }
}

