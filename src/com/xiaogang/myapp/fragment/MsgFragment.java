package com.xiaogang.myapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemMsgAdapter;
import com.xiaogang.myapp.adapter.OnClickContentItemListener;
import com.xiaogang.myapp.bean.BaseFragment;
import com.xiaogang.myapp.model.Minemsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 * 消息
 */
public class MsgFragment extends BaseFragment implements View.OnClickListener, OnClickContentItemListener {
    private ItemMsgAdapter adapter;
    private List<Minemsg> lists;
    private ListView lstv_msg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.msg_fragment, null);
        lists = new ArrayList<>();
        lists.add(new Minemsg(R.drawable.head_one,"小刚", "2015-07-28","你好，很高兴见到你"));
        lists.add(new Minemsg(R.drawable.head_two,"lonely孤独", "2015-07-25","要的，要努力一下试试看"));
        lists.add(new Minemsg(R.drawable.head_three,"苍老师", "2015-07-20","duang,duang"));
        lists.add(new Minemsg(R.drawable.head_one,"赵老师", "2015-07-28","日照香炉生紫烟，遥看瀑布挂前川"));
        lists.add(new Minemsg(R.drawable.head_two,"李白", "2015-03-28","要是能重来，我要做李白"));
        lists.add(new Minemsg(R.drawable.head_three,"天边的一抹夕阳", "2012-07-28","要是太刺眼，就把背光调整一下"));
        lists.add(new Minemsg(R.drawable.head_one,"小刚", "2015-07-28","你好，很高兴见到你"));
        lists.add(new Minemsg(R.drawable.head_two,"lonely孤独", "2015-07-25","要的，要努力一下试试看"));
        lists.add(new Minemsg(R.drawable.head_three,"苍老师", "2015-07-20","duang,duang"));
        lists.add(new Minemsg(R.drawable.head_one,"赵老师", "2015-07-28","日照香炉生紫烟，遥看瀑布挂前川"));
        lists.add(new Minemsg(R.drawable.head_two,"李白", "2015-03-28","要是能重来，我要做李白"));
        lists.add(new Minemsg(R.drawable.head_three,"天边的一抹夕阳", "2012-07-28","要是太刺眼，就把背光调整一下"));
        adapter = new ItemMsgAdapter(lists,getActivity());
        lstv_msg = (ListView) view.findViewById(R.id.lstv_msg);
        lstv_msg.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
        return view;
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {

    }

    @Override
    public void onClick(View v) {

    }
}
