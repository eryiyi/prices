package com.xiaogang.myapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.BuddyAdapter;
import com.xiaogang.myapp.adapter.OnClickContentItemListener;
import com.xiaogang.myapp.bean.BaseFragment;

/**
 * Created by Administrator on 2015/7/28.
 * 好友
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener, OnClickContentItemListener {
    ExpandableListView expandablelistview;
    //群组名称
    private String[] group = new String[] { "在线好友", "我的好友", "我的同事"};
    //好友名称
    private String[][] buddy = new String[][] {
            { "元芳", "雷丶小贱", "狄大人"},
            {"高太后", "士兵甲", "士兵乙", "士兵丙" },
            { "艺术家", "叫兽", "攻城师", "职业玩家" }};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_fragment, null);
        expandablelistview= (ExpandableListView) view.findViewById(R.id.buddy_expandablelistview);
//        expandablelistview.setGroupIndicator(this.getResources().getDrawable(R.drawable.button_expandable));
        expandablelistview.setGroupIndicator(null);
        ExpandableListAdapter adapter=new BuddyAdapter(getActivity(),group,buddy);
        expandablelistview.setAdapter(adapter);
        //分组展开
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            public void onGroupExpand(int groupPosition) {
            }
        });
        //分组关闭
        expandablelistview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){
            public void onGroupCollapse(int groupPosition) {
            }
        });
        //子项单击
        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int groupPosition, int childPosition, long arg4) {
                Toast.makeText(getActivity(),
                        group[groupPosition] + " : " + buddy[groupPosition][childPosition],
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return view;
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {

    }

    @Override
    public void onClick(View v) {

    }
}
