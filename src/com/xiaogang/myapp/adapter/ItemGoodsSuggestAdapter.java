package com.xiaogang.myapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.model.Chengdu;
import com.xiaogang.myapp.model.GoodsSuggest;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/5
 * Time: 16:17
 * 商品搜索
 */
public class ItemGoodsSuggestAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<GoodsSuggest> lists;
    private Context mContext;
    private Resources res;
    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    public ItemGoodsSuggestAdapter(List<GoodsSuggest> lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
        res = mContext.getResources();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_suggest, null);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_hours = (TextView) convertView.findViewById(R.id.item_hours);
            holder.item_recommend = (TextView) convertView.findViewById(R.id.item_recommend);
            holder.item_know = (TextView) convertView.findViewById(R.id.item_know);
            holder.item_idea = (TextView) convertView.findViewById(R.id.item_idea);
            holder.item_merit = (TextView) convertView.findViewById(R.id.item_merit);
            holder.item_defect = (TextView) convertView.findViewById(R.id.item_defect);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.zhichi = (TextView) convertView.findViewById(R.id.zhichi);
            holder.fandui = (TextView) convertView.findViewById(R.id.fandui);
            holder.item_grade = (RatingBar) convertView.findViewById(R.id.item_grade);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsSuggest cell = lists.get(position);
        if (cell != null) {
            holder.item_title.setText(cell.getUser());
            holder.item_hours.setText(res.getString(R.string.shiyongshijian) + cell.getHours());
            switch (Integer.parseInt(cell.getRecommend()==null?"1":cell.getRecommend())){
                case 1:
                    holder.item_recommend.setText(res.getString(R.string.tuijianchegndu) + res.getString(R.string.jilituijian) );
                    break;
                case 2:
                    holder.item_recommend.setText(res.getString(R.string.tuijianchegndu) + res.getString(R.string.tuijian) );
                    break;
                case 3:
                    holder.item_recommend.setText(res.getString(R.string.tuijianchegndu) + res.getString(R.string.butuijian) );
                    break;
                case 4:
                    holder.item_recommend.setText(res.getString(R.string.tuijianchegndu) + res.getString(R.string.meiyouyijian) );
                    break;
            }

            holder.item_idea.setText(res.getString(R.string.item_idea) +cell.getIdea());
            holder.item_merit.setText(res.getString(R.string.item_merit) + cell.getMerit());
            holder.item_defect.setText(res.getString(R.string.item_defect) + cell.getDefect());
            switch (Integer.parseInt(cell.getKnow()==null?"":cell.getKnow())){
                case 1:
                    holder.item_know.setText( res.getString(R.string.shuxichegndu)+res.getString(R.string.zhuanye) );
                    break;
                case 2:
                    holder.item_know.setText(res.getString(R.string.shuxichegndu)+res.getString(R.string.jinjie) );
                    break;
                case 3:
                    holder.item_know.setText(res.getString(R.string.shuxichegndu)+res.getString(R.string.yiban) );
                    break;
                case 4:
                    holder.item_know.setText(res.getString(R.string.shuxichegndu)+res.getString(R.string.xinshou) );
                    break;

            }

            holder.item_dateline.setText(res.getString(R.string.pinglunshijian) + (cell.getDate()==null?"":cell.getDate()));
            holder.item_grade.setNumStars(Integer.parseInt(cell.getGrade()==null?"0":cell.getGrade()));
            holder.zhichi.setText(String.format(res.getString(R.string.zhichi), cell.getApprove()));
            holder.fandui.setText(String.format(res.getString(R.string.fandui), cell.getOppose()));

            holder.zhichi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 1, null);
                }
            }); holder.fandui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 2, null);
                }
            });
        }

        return convertView;
    }
    class ViewHolder{
        TextView item_title;
        TextView item_hours;
        TextView item_recommend;
        TextView item_idea;
        TextView item_merit;
        TextView item_defect;
        TextView item_know;
        TextView item_dateline;
        RatingBar item_grade;

        TextView zhichi;
        TextView fandui;
    }
}

