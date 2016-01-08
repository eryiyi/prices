package com.xiaogang.myapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.Pro_type_adapter;
import com.xiaogang.myapp.bean.BaseFragment;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.TypeSmallData;
import com.xiaogang.myapp.model.GoodsTypeSmall;
import com.xiaogang.myapp.model.TypeBig;
import com.xiaogang.myapp.ui.MineSmallTypeActivity;
import com.xiaogang.myapp.ui.SerachGoodsActivity;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_pro_type extends BaseFragment {
	private List<GoodsTypeSmall> list = new ArrayList<>();
	private ImageView hint_img;
	private GridView listView;
	private Pro_type_adapter adapter;
	private TypeBig typbige;
	private ProgressBar progressBar;
	private String typename;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pro_type, null);
		progressBar=(ProgressBar) view.findViewById(R.id.progressBar);
		hint_img=(ImageView) view.findViewById(R.id.hint_img);
		listView = (GridView) view.findViewById(R.id.listView);
		typename=getArguments().getString("typename");
		typbige = getArguments().getParcelable("goodsTypeBig");

		((TextView)view.findViewById(R.id.toptype)).setText(typename);
		GetTypeList();
		adapter=new Pro_type_adapter(getActivity(), list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(getActivity(), MineSmallTypeActivity.class);
				GoodsTypeSmall goodsTypeSmall = list.get(arg2);
				intent.putExtra("id", goodsTypeSmall.getId());
				intent.putExtra("name", goodsTypeSmall.getNav_name());
				startActivity(intent);
			}
		});
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		getSmallType ();
		return view;
	}
	
	
	private void GetTypeList() {
		progressBar.setVisibility(View.GONE);
	}


	//获得类别
	private void getSmallType() {
		String uri = InternetURL.CHILD_NAV_URL +"?id=" + typbige.getId();
		StringRequest request = new StringRequest(
				Request.Method.GET,
				uri,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						if (StringUtil.isJson(s)) {
							try {
								JSONObject jo = new JSONObject(s);
								String code1 =  jo.getString("code");
								if(Integer.parseInt(code1) == 200){
									TypeSmallData data = getGson().fromJson(s, TypeSmallData.class);
									list.clear();
									list.addAll(data.getData());
									adapter.notifyDataSetChanged();
								}else {
									Toast.makeText(getActivity(), R.string.get_no_data, Toast.LENGTH_SHORT).show();
								}
							}catch (JSONException e){
								e.printStackTrace();
							}
						} else {
							Toast.makeText(getActivity(), R.string.get_no_data, Toast.LENGTH_SHORT).show();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Toast.makeText(getActivity(), R.string.get_no_data, Toast.LENGTH_SHORT).show();
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				return params;
			}
		};
		getRequestQueue().add(request);
	}
	
	
}
