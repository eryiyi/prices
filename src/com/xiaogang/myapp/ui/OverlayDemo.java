package com.xiaogang.myapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.xiaogang.myapp.PricesApplication;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.NearByGoodsData;
import com.xiaogang.myapp.model.NearByGoods;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.SelectDistanceWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 覆盖物的用法
 */
public class OverlayDemo extends BaseActivity implements View.OnClickListener {
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private List<Marker> Markers = new ArrayList<>();
	private List<LatLng> LatLngs = new ArrayList<>();//有经纬度的坐标
	private List<LatLng> LatLngsMine = new ArrayList<>();//符合条件的坐标
	ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();//图片
	List<NearByGoods> listsNear = new ArrayList<NearByGoods>();//全部的坐标

	private InfoWindow mInfoWindow;
	private ImageView switch_button;
	private RelativeLayout pop_layout;
	private SeekBar seekBar;
	private SeekBar seekBar_two;
	private boolean flag = true;

	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.green_location);
	BitmapDescriptor bdGround = BitmapDescriptorFactory
			.fromResource(R.drawable.ground_overlay);

	private TextView near_one;//附近KM
	private TextView near_two;//坐标几个
	private SelectDistanceWindow selectDistanceWindow;
	Double distanceKm = 3.0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerBoradcastReceiver();
		setContentView(R.layout.activity_overlay);
		initView();

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		MydrawPointCurrentLocation((PricesApplication.lat==null?0.0:PricesApplication.lat), (PricesApplication.lon==null?0.0:PricesApplication.lon));
		getData();
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				OnInfoWindowClickListener listener = null;
//				if (marker == mMarkerA || marker == mMarkerD) {
//					button.setText("更改位置");
//					listener = new OnInfoWindowClickListener() {
//						public void onInfoWindowClick() {
//							LatLng ll = marker.getPosition();
//							LatLng llNew = new LatLng(ll.latitude + 0.005,
//									ll.longitude + 0.005);
//							marker.setPosition(llNew);
//							mBaiduMap.hideInfoWindow();
//						}
//					};
//					LatLng ll = marker.getPosition();
//					mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
//					mBaiduMap.showInfoWindow(mInfoWindow);
//				} else if (marker == mMarkerB) {
//					button.setText("更改图标");
//					button.setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							marker.setIcon(bd);
//							mBaiduMap.hideInfoWindow();
//						}
//					});
//					LatLng ll = marker.getPosition();
//					mInfoWindow = new InfoWindow(button, ll, -47);
//					mBaiduMap.showInfoWindow(mInfoWindow);
//				} else if (marker == mMarkerC) {
//					button.setText("删除");
//					button.setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							marker.remove();
//							mBaiduMap.hideInfoWindow();
//						}
//					});
//					LatLng ll = marker.getPosition();
//					mInfoWindow = new InfoWindow(button, ll, -47);
//					mBaiduMap.showInfoWindow(mInfoWindow);
//				}
				return true;
			}
		});
	}

	private void initView() {
		near_one = (TextView) this.findViewById(R.id.near_one);
		near_two = (TextView) this.findViewById(R.id.near_two);
		switch_button = (ImageView) this.findViewById(R.id.switch_button);
		switch_button.setOnClickListener(this);
		pop_layout = (RelativeLayout) this.findViewById(R.id.pop_layout);
		pop_layout.setVisibility(View.GONE);

		seekBar = (SeekBar) this.findViewById(R.id.seekBar);
		seekBar_two = (SeekBar) this.findViewById(R.id.seekBar_two);
		seekBar.setOnSeekBarChangeListener(seekbarChangeListener);
		seekBar.setOnSeekBarChangeListener(seekbarChangeListenerTwo);
	}


	public void initOverlay() {
		LatLngsMine.clear();
		if(LatLngs != null){
			for(LatLng latLng :LatLngs){
				Double distanceMine = StringUtil.GetShortDistance(PricesApplication.lon, PricesApplication.lat, latLng.longitude, latLng.latitude);
				if(distanceMine < distanceKm){
					LatLngsMine.add(latLng);
				}
			}
		}

		// add marker overlay
		if(LatLngsMine != null && LatLngsMine.size() > 0){
			for(LatLng latLng : LatLngsMine){
				OverlayOptions ooA = new MarkerOptions().position(latLng).icon(bd)
						.zIndex(9).draggable(true);
				Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
				Markers.add(mMarkerA);
				giflist.add(bd);
			}
		}

		near_two.setText(String.format(getResources().getString(R.string.txt006), LatLngsMine.size()));
		near_one.setText(String.format(getResources().getString(R.string.fujin3), distanceKm));
		// add ground overlay
		LatLng southwest = new LatLng(PricesApplication.lat-0.02, PricesApplication.lon-0.03);
		LatLng northeast = new LatLng(PricesApplication.lat, PricesApplication.lon);

		LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
				.include(southwest).build();

		OverlayOptions ooGround = new GroundOverlayOptions()
				.positionFromBounds(bounds).image(bdGround).transparency(0.8f);
		mBaiduMap.addOverlay(ooGround);

		MapStatusUpdate u = MapStatusUpdateFactory
				.newLatLng(bounds.getCenter());
		mBaiduMap.setMapStatus(u);

		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {
				Toast.makeText(
						OverlayDemo.this,
						"拖拽结束，新位置：" + marker.getPosition().latitude + ", "
								+ marker.getPosition().longitude,
						Toast.LENGTH_LONG).show();
			}

			public void onMarkerDragStart(Marker marker) {
			}
		});
	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
//	public void clearOverlay(View view) {
//		mBaiduMap.clear();
//	}

	/**
	 * 重新添加Overlay
	 */
//	public void resetOverlay(View view) {
//		clearOverlay(null);
//		initOverlay();
//	}

	@Override
	public void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		if(giflist != null && giflist.size() > 0){
			for(BitmapDescriptor bd: giflist){
				bd.recycle();
			}
		}
		bd.recycle();
		bdGround.recycle();
		unregisterReceiver(mBroadcastReceiver);
	}

	public void back(View view){
		finish();
	}

	public void MydrawPointCurrentLocation(Double lat, Double lng){
		//定义Maker坐标点
		LatLng point = new LatLng(lat,lng);
		//构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.red_location);
		//构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions()
				.position(point)
				.icon(bitmap);
		//在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);
	}

	private void getData() {
		String uri = InternetURL.GET_ALL_SHANGJIA_NEARBY;
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
									listsNear.clear();
									NearByGoodsData data = getGson().fromJson(s, NearByGoodsData.class);
									listsNear.addAll(data.getData());
									if(listsNear.size() > 0){
										for(NearByGoods nearByGoods : listsNear){
											if(nearByGoods != null && !StringUtil.isNullOrEmpty(nearByGoods.getLat()) && !StringUtil.isNullOrEmpty(nearByGoods.getLng())){}
											LatLngs.add(new LatLng(Double.valueOf((nearByGoods.getLat().equals("")?"0.0":nearByGoods.getLat())), Double.valueOf((nearByGoods.getLng().equals("")?"0.0":nearByGoods.getLng()))));
										}
									}
									initOverlay();
								}else  if(Integer.parseInt(code1) == 400){

									Toast.makeText(OverlayDemo.this, R.string.has_no_data, Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(OverlayDemo.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
								}
							}catch (JSONException e){
								e.printStackTrace();
							}
						} else {
							Toast.makeText(OverlayDemo.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Toast.makeText(OverlayDemo.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.switch_button:
				//选择
				if(flag){
					pop_layout.setVisibility(View.VISIBLE);
				}else {
					pop_layout.setVisibility(View.GONE);
				}
				break;
		}
	}

	// 选择弹框地址
	private void ShowPickDialog() {
		selectDistanceWindow = new SelectDistanceWindow(OverlayDemo.this, itemsOnClick);
		//显示窗口
		selectDistanceWindow.showAtLocation(OverlayDemo.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

	}
	//为弹出窗口实现监听类
	private View.OnClickListener itemsOnClick = new View.OnClickListener() {

		public void onClick(View v) {
			selectDistanceWindow.dismiss();
			switch (v.getId()) {
//                case R.id.mapstorage: {
//                    Intent mapstorage = new Intent(Intent.ACTION_PICK, null);
//                    mapstorage.setDataAndType(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                            "image/*");
//                    startActivityForResult(mapstorage, 1);
//                }
//                break;
				default:
					break;
			}
		}
	};


	//广播接收动作
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if(action.equals("send_seek")){
//				String key_seek =  intent.getExtras().getString("key_seek");
//				if(!StringUtil.isNullOrEmpty(key_seek)){
//
//				}
//			}
		}
	};

	//注册广播
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
//		myIntentFilter.addAction("send_seek");
		//注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}


	private SeekBar.OnSeekBarChangeListener seekbarChangeListener = new SeekBar.OnSeekBarChangeListener() {

		// 停止拖动时执行
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		// 在进度开始改变时执行
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
//            textView_two.setText("进度开始改变");
		}

		// 当进度发生改变时执行
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
									  boolean fromUser) {
//            textView_two.setText("正在进行拖动操作，还没有停下来一直再拖动");
			Message message = new Message();

			Bundle bundle = new Bundle();// 存放数据

			float pro = seekBar.getProgress();

			float num = seekBar.getMax();

			float result = (pro / num) * 100;
			bundle.putFloat("key", result);

			message.setData(bundle);

			message.what = 0;

			handler.sendMessage(message);
//
//            Toast.makeText(context,
//                    String.valueOf(seekBar.getProgress()),
//                    Toast.LENGTH_SHORT).show();


		}
	};

	/**
	 * 用Handler来更新UI
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//            textView_one.setText(msg.getData().getFloat("key") + "/100");
//			Toast.makeText(context, msg.getData().getFloat("key")+"/100" , Toast.LENGTH_SHORT).show();
		}
	};

	private SeekBar.OnSeekBarChangeListener seekbarChangeListenerTwo = new SeekBar.OnSeekBarChangeListener() {

		// 停止拖动时执行
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
//            textView_two.setText("停止拖动了！");

		}

		// 在进度开始改变时执行
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
//            textView_two.setText("进度开始改变");
		}

		// 当进度发生改变时执行
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
									  boolean fromUser) {
//            textView_two.setText("正在进行拖动操作，还没有停下来一直再拖动");
			Message message = new Message();

			Bundle bundle = new Bundle();// 存放数据

			float pro = seekBar.getProgress();

			float num = seekBar.getMax();

			float result = (pro / num) * 100;
			bundle.putFloat("key", result);

			message.setData(bundle);

			message.what = 0;

			handlertwo.sendMessage(message);

		}
	};

	/**
	 * 用Handler来更新UI
	 */
	private Handler handlertwo = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//            Toast.makeText(context, msg.getData().getFloat("key")+"/1000" , Toast.LENGTH_SHORT).show();
			distanceKm = Double.valueOf( msg.getData().getFloat("key")/100*6);
			BigDecimal bd = new BigDecimal(distanceKm);
			distanceKm = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			initOverlay();
		}
	};

}
