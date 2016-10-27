package com.tricker.recordmoney;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.tricker.recordmoney.util.TrickerUtils;


public class MapFragment extends Fragment implements LocationSource, AMapLocationListener, OnCheckedChangeListener {
	private static final String ARG_SECTION_NUMBER = "section_number";

	// private OnFragmentInteractionListener mListener;
	private MapView mapView = null;
	private AMap aMap = null;
	// 声明mLocationOption对象
	public AMapLocationClientOption mLocationOption = null;

	/**
	 * 定位监听
	 */

	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;


	public static MapFragment newInstance(int sectionNumber) {
		MapFragment fragment = new MapFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public MapFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		// 获取地图控件引用
		mapView = (MapView) view.findViewById(R.id.map);
		// 在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
		mapView.onCreate(savedInstanceState);
		init();

		return view;
	}
	/**
	 * 初始化
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
//		mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
//		mGPSModeGroup.setOnCheckedChangeListener(this);
//		mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);
//		mLocationErrText.setVisibility(View.GONE);
	}
	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		//设置地图缩放比例
		aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		// if (mListener != null) {
		// mListener.onFragmentInteraction(uri);
		// }
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// try {
		// mListener = (OnFragmentInteractionListener) activity;
		// } catch (ClassCastException e) {
		// throw new ClassCastException(activity.toString() + " must implement
		// OnFragmentInteractionListener");
		// }
//		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}

	@Override
	public void onDetach() {
		super.onDetach();
		// mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	// public interface OnFragmentInteractionListener {
	// // TODO: Update argument type and name
	// public void onFragmentInteraction(Uri uri);
	// }
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapView.onDestroy();
		if(null != mlocationClient){
			mlocationClient.onDestroy();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
		mapView.onPause();
		deactivate();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState
		// (outState)，实现地图生命周期管理
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
//		TrickerUtils.showToast(getActivity(), amapLocation.getAddress());
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
//				mLocationErrText.setVisibility(View.GONE);
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
				TrickerUtils.showToast(getActivity(), errText);
//				mLocationErrText.setVisibility(View.VISIBLE);
//				mLocationErrText.setText(errText);
			}
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(getActivity());
			mLocationOption = new AMapLocationClientOption();
//			mLocationOption.setNeedAddress(true);
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}

	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;

	}

}
