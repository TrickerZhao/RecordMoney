package com.tricker.recordmoney;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.tricker.recordmoney.db.BaseDao;
import com.tricker.recordmoney.db.TrickerDB;
import com.tricker.recordmoney.model.Marry;
import com.tricker.recordmoney.model.Project;
import com.tricker.recordmoney.model.Sale;
import com.tricker.recordmoney.util.Constant;
import com.tricker.recordmoney.util.TrickerUtils;

import java.util.Date;

import static com.tricker.recordmoney.R.id.marry;


//import android.support.v4.app.Fragment;


public class RecordFragment extends Fragment implements OnClickListener, LocationSource, AMapLocationListener, AdapterView.OnItemSelectedListener {
	private int type= -1;
	private TableLayout tableLayoutRent,tableLayoutMarry,tableSale;
	private EditText editDate, editMoney, editProject, editRemark,editName,editMoney1,editMoney2,editRemark1;
	private EditText editSaleDate,editSaleWeek,editSaleMoney,editSaleRemark;
	private Spinner editPercent, editState,editType,editState1,editSaleType;
	// private SimpleDateFormat format;
	private Button btnSave;
	private TextView txtLocation;
	private static boolean isEdit;
	private LocationManager locationManager;
	private String provider;
	private static final int SHOW_RESPONSE = 0;
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	// 声明mLocationOption对象
	public AMapLocationClientOption mLocationOption = null;

	/**
	 * 定位监听
	 */

	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static RecordFragment newInstance(int sectionNumber, Cursor cursor, int type) {
		RecordFragment fragment = new RecordFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		if (cursor != null) {// 说明是从查询界面点击回来的，需要修改
			isEdit = true;
			args.putInt("types", type);// type
			if(type== Constant.RENT){
				args.putInt("_id", cursor.getInt(cursor.getColumnIndex("_id")));// _id
				args.putString("project", cursor.getString(cursor.getColumnIndex("project")));// project
				args.putString("money", cursor.getString(cursor.getColumnIndex("money")));// money
				args.putString("date", cursor.getString(cursor.getColumnIndex("date")));// date
				args.putString("percent", cursor.getString(cursor.getColumnIndex("percent")));// percent
				args.putString("remark", cursor.getString(cursor.getColumnIndex("remark")));// remark
				args.putString("state", cursor.getString(cursor.getColumnIndex("state")));// state
			}else if(type==Constant.MARRY){
				args.putInt("_id", cursor.getInt(cursor.getColumnIndex("_id")));// _id
				args.putString("name", cursor.getString(cursor.getColumnIndex("name")));// project
				args.putString("payMoney", cursor.getString(cursor.getColumnIndex("payMoney")));// money
				args.putString("getMoney", cursor.getString(cursor.getColumnIndex("getMoney")));// date
				args.putString("remark", cursor.getString(cursor.getColumnIndex("remark")));// remark
				args.putString("state", cursor.getString(cursor.getColumnIndex("state")));// state
			}else if(type==Constant.SALE){
				args.putInt("_id", cursor.getInt(cursor.getColumnIndex("_id")));// _id
				args.putString("date", cursor.getString(cursor.getColumnIndex("date")));// project
				args.putString("week", cursor.getString(cursor.getColumnIndex("week")));// money
				args.putString("money", cursor.getString(cursor.getColumnIndex("money")));// date
				args.putString("remark", cursor.getString(cursor.getColumnIndex("remark")));// remark
				args.putString("type", cursor.getString(cursor.getColumnIndex("type")));// state
			}
		} else {
			isEdit = false;
		}
		fragment.setArguments(args);
		return fragment;
	}

	public RecordFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_record, container, false);
		findViews(rootView);
		if (isEdit) {// 编辑
			setValues();
		}
		//利用安卓API加google地图定位，并不是非常准确，用高德地图代替
		/*locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = locationManager.getProviders(true);
		if (providers.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
		} else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		}else if(providers.contains(LocationManager.PASSIVE_PROVIDER)){
			provider = LocationManager.PASSIVE_PROVIDER;
		}
		else {
			TrickerUtils.showToast(getActivity(), "尚未开启任何定位服务！");
			provider=null;
		}
		if (provider != null) {
//			locationManager.setTestProviderEnabled("gps",true);
			Location location = locationManager.getLastKnownLocation(provider);
			if (location != null) {
				showLocation(location);
			}else{
				TrickerUtils.showToast(getActivity(), provider+"暂未获取到位置信息");
			}
			locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
		}*/

		mlocationClient = new AMapLocationClient(getActivity());
		mLocationOption = new AMapLocationClientOption();
		//设置定位监听
		mlocationClient.setLocationListener(this);
		//设置为高精度定位模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);

		mlocationClient.setLocationOption(mLocationOption);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用onDestroy()方法
		// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
		mlocationClient.startLocation();
//		mLocationOption.setOnceLocation(false);
		AMapLocation amapLocation= mlocationClient.getLastKnownLocation();
		if (amapLocation != null
				&& amapLocation.getErrorCode() == 0) {
			txtLocation.setText("1:"+amapLocation.getAddress());
		}
		return rootView;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//		mapView.onDestroy();
		if (null != mlocationClient) {
			mlocationClient.onDestroy();
		}
		/*if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
//			locationManager.setTestProviderEnabled("gps",false);
		}*/
	}
	@Override
	public void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
//		mapView.onResume();
	}
	@Override
	public void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
//		mapView.onPause();
		deactivate();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState
		// (outState)，实现地图生命周期管理
//		mapView.onSaveInstanceState(outState);
	}

	/*private void showLocation(final Location location) {
		if (location == null) {
			TrickerUtils.showToast(getActivity(), "暂未获取到位置信息");
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					StringBuilder url = new StringBuilder();
					url.append("http://maps.google.cn/maps/api/geocode/json?latlng=");
					url.append(location.getLatitude()).append(",");
					url.append(location.getLongitude());
					url.append("&sensor=false");
					// TrickerUtils.showToast(getActivity(), url.toString());
					HttpURLConnection connection = null;
					URL uUrl = new URL(url.toString());
					connection = (HttpURLConnection) uUrl.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);

					}
					// Log.e("tricker", response.toString());
					JSONObject json = new JSONObject(response.toString());
					JSONArray resultArray = json.getJSONArray("results");
					if (resultArray.length() > 0) {
						JSONObject subObject = resultArray.getJSONObject(0);
						String address = subObject.getString("formatted_address");
						Message message = new Message();
						message.what = SHOW_RESPONSE;
						message.obj = address;
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}*/

	// private Handler handler = new Handler(new Handler.Callback() {
	// @Override
	// public boolean handleMessage(Message msg) {
	// return false;
	// }
	// }) ;

	/*private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
//			TrickerUtils.showToast(getActivity(), "位置改变！");
			switch (msg.what) {
			case SHOW_RESPONSE:
				String response = msg.obj.toString();
				txtLocation.setText(response);
				break;

			default:
				break;
			}
		};
	};*/
	/*private LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {
			Location location = locationManager.getLastKnownLocation(provider);
			if(location!=null){
				showLocation(location);
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
			TrickerUtils.showToast(getActivity(), "你刚关闭了【"+provider+"】定位服务");
		}

		@Override
		public void onLocationChanged(Location location) {
			showLocation(location);

		}
	};*/

	private void setValues() {
		type=getArguments().getInt("types");
//		setVisible(type);
		editType.setSelection(type);
		editType.setEnabled(false);//编辑不允许修改类型
		if(type==Constant.RENT){
			editDate.setText(getArguments().getString("date"));
			editMoney.setText(getArguments().getString("money"));
			editProject.setText(getArguments().getString("project"));
			String percent = getArguments().getString("percent");
			String state = getArguments().getString("state");
			int position = 0;
			if (!TextUtils.isEmpty(percent) && percent.equals("1/2")) {
				position = 1;
			}
			editPercent.setSelection(position);
			editRemark.setText(getArguments().getString("remark"));

			position = 0;
			if (!TextUtils.isEmpty(state) && state.equals("已结算")) {
				position = 1;
			}
			editState.setSelection(position);
		}else if(type==Constant.MARRY){
			editName.setText(getArguments().getString("name"));
			editMoney1.setText(getArguments().getString("getMoney"));
			editMoney2.setText(getArguments().getString("payMoney"));
			editRemark1.setText(getArguments().getString("remark"));
			String state = getArguments().getString("state");
			int position = 0;
			if (!TextUtils.isEmpty(state) && state.equals("已结算")) {
				position = 1;
			}
			editState.setSelection(position);
		}else if(type==Constant.SALE){
			editSaleDate.setText(getArguments().getString("date"));
			editSaleMoney.setText(getArguments().getString("money"));
			editSaleWeek.setText(getArguments().getString("week"));
			editSaleRemark.setText(getArguments().getString("remark"));
			editSaleType.setSelection(TrickerUtils.getItemPosition(getArguments().getString("type")));
		}


		btnSave.setText("修改");
	}

	private void findViews(View rootView) {
		//rent
		editDate = (EditText) rootView.findViewById(R.id.editDate);
		editMoney = (EditText) rootView.findViewById(R.id.editMoney);
		editProject = (EditText) rootView.findViewById(R.id.editProject);
		editPercent = (Spinner) rootView.findViewById(R.id.editPercent);
		btnSave = (Button) rootView.findViewById(R.id.btnSave);
		editRemark = (EditText) rootView.findViewById(R.id.editRemark);
		editState = (Spinner) rootView.findViewById(R.id.editState);
		editType= (Spinner) rootView.findViewById(R.id.editType);
		txtLocation = (TextView) rootView.findViewById(R.id.txtLocation);
		tableLayoutRent = (TableLayout) rootView.findViewById(R.id.project);
		tableLayoutMarry = (TableLayout) rootView.findViewById(marry);
		tableSale = (TableLayout) rootView.findViewById(R.id.sale);
		btnSave.setText("保存");
		//marry
		editName = (EditText) rootView.findViewById(R.id.editName);
		editMoney1 = (EditText) rootView.findViewById(R.id.editMoney1);
		editMoney2 = (EditText) rootView.findViewById(R.id.editMoney2);
		editState1 = (Spinner) rootView.findViewById(R.id.editState1);
		editRemark1 = (EditText) rootView.findViewById(R.id.editRemark1);
		editType.setOnItemSelectedListener(this);
		//Sale
		editSaleDate= (EditText) rootView.findViewById(R.id.editSaleDate);
		editSaleWeek= (EditText) rootView.findViewById(R.id.editWeek);
		editSaleType= (Spinner) rootView.findViewById(R.id.editSaleType);
		editSaleMoney= (EditText) rootView.findViewById(R.id.editSaleMoney);
		editSaleRemark= (EditText) rootView.findViewById(R.id.editSaleRemark);

		if(!MyApplication.getUser().getName().equals("Tricker")){
			editType.setSelection(2);
//			editType.setClickable(false);
			editType.setEnabled(false);//设置不可编辑
			editSaleMoney.requestFocus();
		}
		editSaleDate.setText(TrickerUtils.getSystemDateTime());
		editSaleWeek.setText(TrickerUtils.getDayOfWeek());
		// format = new SimpleDateFormat("yyyy/MM/dd");
		// String date = format.format(Calendar.getInstance().getTime());
		editDate.setText(TrickerUtils.getSystemDate());
		editDate.setKeyListener(null);// 相当于设置不可编辑
		editDate.setOnClickListener(this);
		editSaleDate.setKeyListener(null);// 相当于设置不可编辑
		editSaleDate.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		txtLocation.setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//注释
//		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnSave:
				saveData();
				break;
			case R.id.editDate:
				// selectDate();
				TrickerUtils.selectDate(getActivity(), editDate);
				break;
			case R.id.txtLocation:
//				MainActivity mainActivity = (MainActivity) getActivity();
				//直接打开地图页
				getFragmentManager().beginTransaction().replace(R.id.container, MapFragment.newInstance(1))
						.commit();
//				mainActivity.getmNavigationDrawerFragment().selectItem(2);
//				//改变标题
//				mainActivity.onSectionAttached(3);
//				mainActivity.restoreActionBar();
				break;
			case R.id.editSaleDate:
				DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						Date date = new Date(year-1900,month,dayOfMonth);

						String dateTime = TrickerUtils.getDateTimeFormat().format(date);
						editSaleDate.setText(dateTime);
						editSaleWeek.setText(TrickerUtils.getDayOfWeek(date));
					}
				};
				TrickerUtils.selectDate(getActivity(), editDate,callback);
				break;
			default:
				break;
		}
	}

	private void saveData() {
		BaseDao dao = new BaseDao(getActivity());
//		SQLiteDatabase db = dao.getWritableDatabase();
		//判断是保存房租还是份子钱
		if(editType.getSelectedItem().equals("房租")){
			String projectName = this.editProject.getText().toString();
			String money = this.editMoney.getText().toString();
			String editDate = this.editDate.getText().toString();
			String editPercent = this.editPercent.getSelectedItem().toString();
			String editRemark = this.editRemark.getText().toString();
			String editState = this.editState.getSelectedItem().toString();
			if (TextUtils.isEmpty(money)) {
				TrickerUtils.showToast(getActivity(), "金额不能为空！");
				return;
			}
			if (TextUtils.isEmpty(projectName)) {
				TrickerUtils.showToast(getActivity(), "项目不能为空！");
				return;
			}
			if (TextUtils.isEmpty(editPercent)) {
				TrickerUtils.showToast(getActivity(), "占比不能为空！");
				return;
			}
			Project project = new Project();
			project.setProject(projectName);
			project.setMoney(money);
			project.setDate(editDate);
			project.setPercent(editPercent);
			project.setRemark(editRemark);
			project.setState(editState);
			if (isEdit) {
				project.setId(getArguments().getInt("_id"));
				TrickerDB.getInstance(getActivity()).saveProject(project,true);
				// TrickerUtils.showToast(getActivity(), "修改成功！");
//				MainActivity mainActivity = (MainActivity) getActivity();
//				mainActivity.setType(this.type);
//				mainActivity.getmNavigationDrawerFragment().selectItem(1);
				getFragmentManager().beginTransaction().replace(R.id.container, QueryFragment.newInstance(1,type))
						.commit();
			} else {
				TrickerDB.getInstance(getActivity()).saveProject(project);
				TrickerUtils.showToast(getActivity(), "保存成功！");
			}

		}else if(editType.getSelectedItem().equals("份子钱")){
			String name = this.editName.getText().toString();
			String money1 = this.editMoney1.getText().toString();
			String money2 = this.editMoney2.getText().toString();
			String editRemark1 = this.editRemark1.getText().toString();
			String editState1 = this.editState1.getSelectedItem().toString();
			if (TextUtils.isEmpty(money1)) {
				TrickerUtils.showToast(getActivity(), "获得金额不能为空！");
				return;
			}
			if (TextUtils.isEmpty(name)) {
				TrickerUtils.showToast(getActivity(), "姓名不能为空！");
				return;
			}
			Marry marry = new Marry();
			marry.setName(name);
			marry.setGetMoney(money1);
			marry.setPayMoney(money2);
			marry.setRemark(editRemark1);
			marry.setState(editState1);
			if (isEdit) {
				marry.setId(getArguments().getInt("_id"));
				TrickerDB.getInstance(getActivity()).saveMarry(marry,true);
//				MainActivity mainActivity = (MainActivity) getActivity();
//				mainActivity.setType(this.type);
//				mainActivity.getmNavigationDrawerFragment().selectItem(1);
				getFragmentManager().beginTransaction().replace(R.id.container, QueryFragment.newInstance(1,type))
						.commit();
			} else {
				TrickerDB.getInstance(getActivity()).saveMarry(marry);
				TrickerUtils.showToast(getActivity(), "保存成功！");
			}
		}else if(editType.getSelectedItem().equals("销售额")){
			String date = this.editSaleDate.getText().toString();
			String week = this.editSaleWeek.getText().toString();
			String money = this.editSaleMoney.getText().toString();
			String remark = this.editSaleRemark.getText().toString();
			String type = this.editSaleType.getSelectedItem().toString();
			if (TextUtils.isEmpty(money)) {
				TrickerUtils.showToast(getActivity(), "金额不能为空！");
				return;
			}
			Sale sale = new Sale();
			sale.setDate(date);
			sale.setMoney(money);
			sale.setRemark(remark);
			sale.setType(type);
			sale.setWeek(week);
			if (isEdit) {
				sale.setId(getArguments().getInt("_id"));
				TrickerDB.getInstance(getActivity()).saveSale(sale,true);
//				MainActivity mainActivity = (MainActivity) getActivity();
//				mainActivity.setType(this.type);
//				mainActivity.getmNavigationDrawerFragment().selectItem(1);
				getFragmentManager().beginTransaction().replace(R.id.container, QueryFragment.newInstance(1,this.type)).commit();
			} else {
				TrickerDB.getInstance(getActivity()).saveSale(sale);
				TrickerUtils.showToast(getActivity(), "保存成功！");
			}
		}


//		//无论修改和添加都需要备份当前的最新数据库（提取到增删改里）
//		File fromFile = new File(TrickerUtils.getPath(getActivity(), TrickerUtils.DATABASE_PATH));
//		File toFile = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"tricker"+File.separator+"tricker.db");
//		String result =TrickerUtils.copyfile(fromFile, toFile , true);
//		TrickerUtils.showToast(this,Environment.getExternalStorageDirectory().getPath());
//		TrickerUtils.showToast(this, result);
		// getActivity().finish();
	}

	/**
	 * 提取该方法到TrickerUtils
	 */
	/*
	 * private void selectDate() { int year =
	 * Calendar.getInstance().get(Calendar.YEAR); int month =
	 * Calendar.getInstance().get(Calendar.MONTH); int day =
	 * Calendar.getInstance().get(Calendar.DAY_OF_MONTH); String data
	 * =editDate.getText().toString(); if(!TextUtils.isEmpty(data)){ String
	 * strs[] =data.split("/"); year= Integer.parseInt(strs[0]); month=
	 * Integer.parseInt(strs[1])-1; day= Integer.parseInt(strs[2]); }
	 * OnDateSetListener callBack = new OnDateSetListener() {
	 *
	 * @Override public void onDateSet(DatePicker view, int year, int
	 * monthOfYear, int dayOfMonth) { String date = format.format(new
	 * Date(view.getCalendarView().getDate())); editDate.setText(date); } };
	 * DatePickerDialog dlg = new DatePickerDialog(getActivity(), callBack,
	 * year, month, day); dlg.show(); }
	 */

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(getActivity());
			mLocationOption = new AMapLocationClientOption();
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

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
//				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				txtLocation.setText("2："+amapLocation.getAddress());
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
				TrickerUtils.showToast(getActivity(), errText);
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		this.type = position;
//		TrickerUtils.showToast(getActivity(),"ss");
//		if(position==0){
//			setProjectVisible(true);
//		}else if(position==1){
//			setProjectVisible(false);
//		}
		setVisible(position);
	}

	private void setVisible(int type) {
		if(type== Constant.RENT){
			tableLayoutRent.setVisibility(View.VISIBLE);
			tableLayoutMarry.setVisibility(View.GONE);
			tableSale.setVisibility(View.GONE);
			editMoney.requestFocus();
		}else if(type==Constant.MARRY){
			tableLayoutRent.setVisibility(View.GONE);
			tableLayoutMarry.setVisibility(View.VISIBLE);
			tableSale.setVisibility(View.GONE);
			editName.requestFocus();
		}else if(type==Constant.SALE){
			tableLayoutRent.setVisibility(View.GONE);
			tableLayoutMarry.setVisibility(View.GONE);
			tableSale.setVisibility(View.VISIBLE);
			editSaleMoney.requestFocus();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}