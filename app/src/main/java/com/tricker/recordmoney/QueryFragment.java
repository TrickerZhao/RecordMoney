package com.tricker.recordmoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.tricker.recordmoney.db.BaseDao;
import com.tricker.recordmoney.db.TrickerDB;
import com.tricker.recordmoney.util.Constant;
import com.tricker.recordmoney.util.TrickerUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;


public class QueryFragment extends ListFragment
		implements OnItemLongClickListener, OnKeyListener, OnItemSelectedListener, android.view.View.OnClickListener {
	private SimpleCursorAdapter adapter,adapter2,adapter3;
	private BaseDao dao;
	private TextView txtQueryInfo, txtTotalMoney;
	private EditText editQuery;
	private Spinner spSymbol,editType,editShowMethod;
	private Button btnCount,btnOneKeySet,btnAverage,btnMax,btnMin;
	private LinearLayout saleLayout,rentLayout;
	private Cursor cursor;
	private boolean isShowDetail=true;
	private boolean isShowDay=false;
//	private boolean isRent=true;
	private int type= -1;

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static QueryFragment newInstance(int sectionNumber,int type) {
		QueryFragment fragment = new QueryFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		args.putInt("type", type);

		fragment.setArguments(args);
		return fragment;
	}

	public QueryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.type = getArguments().getInt("type");
		View rootView = inflater.inflate(R.layout.fragment_query, container, false);
		txtQueryInfo = (TextView) rootView.findViewById(R.id.queryInfo);
		txtTotalMoney = (TextView) rootView.findViewById(R.id.countMoney);
		editQuery = (EditText) rootView.findViewById(R.id.editQuery);
		spSymbol = (Spinner) rootView.findViewById(R.id.spSymbol);
		editType = (Spinner) rootView.findViewById(R.id.editType);
		editShowMethod = (Spinner) rootView.findViewById(R.id.editShowMethod);
		btnCount = (Button) rootView.findViewById(R.id.btnCount);
		btnOneKeySet = (Button) rootView.findViewById(R.id.btnOneKeySet);
		btnAverage = (Button) rootView.findViewById(R.id.btnAverageMoney);
		btnMax = (Button) rootView.findViewById(R.id.btnMaxMoney);
		btnMin = (Button) rootView.findViewById(R.id.btnMinMoney);

		saleLayout = (LinearLayout) rootView.findViewById(R.id.saleLayout);
		rentLayout = (LinearLayout) rootView.findViewById(R.id.rentLayout);
		btnCount.setOnClickListener(this);
		btnOneKeySet.setOnClickListener(this);
		btnAverage.setOnClickListener(this);
		btnMax.setOnClickListener(this);
		btnMin.setOnClickListener(this);
		editQuery.setOnClickListener(this);
		editQuery.setOnKeyListener(this);
		spSymbol.setOnItemSelectedListener(this);
		editType.setOnItemSelectedListener(this);
		editShowMethod.setOnItemSelectedListener(this);

//		editShowMethod.setVisibility(View.GONE);
		saleLayout.setVisibility(View.GONE);
		editType.setSelection(type);
		setWidgetVisible(type);
		if(!MyApplication.getUser().getName().equals("Tricker")){
			editQuery.setText(TrickerUtils.getSystemDate());
			editType.setSelection(2);
//			editType.setClickable(false);
			editType.setEnabled(false);//设置不可编辑
		}
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/*dao = new BaseDao(getActivity());
		SQLiteDatabase db = dao.getReadableDatabase();
		String sql = BaseDao.QUERY_ALL + " order by date desc";
		Cursor c = db.rawQuery(sql, null);
		setCursor(c);
		updateTitle(c);

		String[] from = new String[] { "project", "money", "date", "percent", "state" };
		int[] to = new int[] { R.id.txtProject, R.id.txtMoney, R.id.txtDate, R.id.txtPercent, R.id.txtState };
		// adapter = new SimpleCursorAdapter(context, layout, c, from, to);
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_cell, c, from, to,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		setListAdapter(adapter);

		getListView().setOnItemLongClickListener(this);
		db.close();*/

		Cursor cursor = TrickerDB.getInstance(getActivity()).loadProjects();
		Cursor cursor2 =TrickerDB.getInstance(getActivity()).loadMarries();
		Cursor cursor3 =TrickerDB.getInstance(getActivity()).loadSales();
		setCursor(cursor);
		updateTitle(cursor);
		String[] from = new String[] { "project", "money", "date", "percent", "state" };
		int[] to = new int[] { R.id.txtProject, R.id.txtMoney, R.id.txtDate, R.id.txtPercent, R.id.txtState };
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_cell, cursor, from, to,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		String[] from2 = new String[] { "name", "getMoney", "payMoney"};
		int[] to2 = new int[] { R.id.txtProject, R.id.txtMoney,  R.id.txtPercent};
		adapter2 = new SimpleCursorAdapter(getActivity(), R.layout.list_cell, cursor2, from2, to2,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		String[] from3 = new String[] { "type", "money","date" ,"count", "week" };
		int[] to3 = new int[] { R.id.txtProject, R.id.txtMoney,R.id.txtDate,R.id.txtPercent, R.id.txtState};
		adapter3 = new SimpleCursorAdapter(getActivity(), R.layout.list_cell, cursor3, from3, to3,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


		setListAdapter(adapter);

		getListView().setOnItemLongClickListener(this);
	}
	private void updateTitle(Cursor c, int type) {
		// double totalMoney = 0;
		BigDecimal totalMoney = new BigDecimal(0);
		if(type== Constant.RENT){
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				// totalMoney+=c.getDouble(c.getColumnIndex("money"));
				String strMoney = c.getString(c.getColumnIndex("money"));
				totalMoney = totalMoney.add(TrickerUtils.parseToDecimal(strMoney));
			}
		}else if(type==Constant.MARRY){
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				// totalMoney+=c.getDouble(c.getColumnIndex("money"));
				String strMoney = c.getString(c.getColumnIndex("getMoney"));
				totalMoney = totalMoney.add(TrickerUtils.parseToDecimal(strMoney));
			}
		}else if(type==Constant.SALE){
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				String strMoney = c.getString(c.getColumnIndex("money"));
				totalMoney = totalMoney.add(TrickerUtils.parseToDecimal(strMoney));
			}
		}
		totalMoney = TrickerUtils.setScale(totalMoney, 2, RoundingMode.HALF_UP);
		txtQueryInfo.setText("以下是查询信息(共" + c.getCount() + "条)");
		txtTotalMoney.setText("￥"+totalMoney);

//		TrickerUtils.showToast(getActivity(), "数据刷新成功！");
	}
	private void updateTitle(Cursor c) {
		updateTitle(c,Constant.RENT);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
	private long lastTime = 0;
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		MainActivity mainActivity = (MainActivity) getActivity();
		Cursor c = adapter.getCursor();
		int type = Constant.RENT;
		if(editType.getSelectedItem().equals("份子钱")){
			type= Constant.MARRY;
			c = adapter2.getCursor();
		}else if(editType.getSelectedItem().equals("销售额")){
			type = Constant.SALE;
			c = adapter3.getCursor();
		}
		//如果在500毫秒内点击2次就是修改
		if (System.currentTimeMillis() - lastTime < 500&&isShowDetail) {//但是合计是不能修改的
			c.moveToPosition(position);
//			mainActivity.setType(type);
//			mainActivity.setEditCursor(c);
			//直接打开记录页
			getFragmentManager().beginTransaction().replace(R.id.container, RecordFragment.newInstance(1,c,this.type))
					.commit();
//			mainActivity.getmNavigationDrawerFragment().selectItem(0);
//			//改变标题
//			mainActivity.onSectionAttached(1);
//			mainActivity.restoreActionBar();
		}else{
			lastTime = System.currentTimeMillis();
			if(type==Constant.SALE&&!isShowDetail){//由于销售额是合并的，所以需要提示每一项有多少个
				if(!isShowDay){
					String date = c.getString(c.getColumnIndex("date"));
					String saleType = c.getString(c.getColumnIndex("type"));
					String result =TrickerDB.getInstance(getActivity()).getSaleInfo(date,saleType);
					TrickerUtils.showToast(getActivity(),result, Toast.LENGTH_LONG);
//					MainActivity activity = (MainActivity)getActivity();
//					TrickerUtils.showSnackbar(activity.getFab(),result,
//							Snackbar.LENGTH_LONG);
				}else{
					TrickerUtils.showToast(getActivity(),"若需获取详情，请查看相应类型！");
//					MainActivity activity = (MainActivity)getActivity();
//					TrickerUtils.showSnackbar(activity.getFab(),"若需获取详情，请查看相应类型！",
//							Snackbar.LENGTH_SHORT);
				}
			}else{
				TrickerUtils.showToast(getActivity(), c.getString(c.getColumnIndex("remark")));
			}
		}
		/*if(count==3){
			c.moveToPosition(position);
			mainActivity.setEditCursor(c);
			//直接打开记录页
			mainActivity.getmNavigationDrawerFragment().selectItem(0);
			//改变标题
			mainActivity.onSectionAttached(1);
			mainActivity.restoreActionBar();
		}else{
			if(type==Constant.SALE&&!isShowDetail){//由于销售额是合并的，所以需要提示每一项有多少个
				String date = c.getString(c.getColumnIndex("date"));
				String saleType = c.getString(c.getColumnIndex("type"));
				String result =TrickerDB.getInstance(getActivity()).getSaleInfo(date,saleType);
				TrickerUtils.showToast(getActivity(),result, Toast.LENGTH_LONG);
			}else{
				TrickerUtils.showToast(getActivity(), c.getString(c.getColumnIndex("remark")));
			}
		}
*/
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		if(type==Constant.SALE&&!isShowDetail){//Sale查看合计是不能执行删除操作的
			return true;
		}
		final Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("提示");
		builder.setMessage("真的要删除吗？\n该操作不可恢复！！！");
		builder.setPositiveButton("删除", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final EditText password = new EditText(getActivity());
				// password.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
				builder.setTitle("请输入密码");
				builder.setMessage(null);
				builder.setView(password);
				builder.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
						int minute = Calendar.getInstance().get(Calendar.MINUTE);
						int total = hour + minute;
						if (password.getText() != null && password.getText().toString().equals(String.valueOf(total))) {
							//判断是删除份子钱还是房租
							if(type==Constant.RENT){
								Cursor c = adapter.getCursor();
								c.moveToPosition(position);
								int projectId = c.getInt(c.getColumnIndex("_id"));
								TrickerDB.getInstance(getActivity()).deleteProject(projectId);
								refreshView();
							}else if(type==Constant.MARRY){
								Cursor c = adapter2.getCursor();
								c.moveToPosition(position);
								int marryId = c.getInt(c.getColumnIndex("_id"));
								TrickerDB.getInstance(getActivity()).deleteMarry(marryId);
								refreshView(Constant.MARRY);
							}else if(type==Constant.SALE){
//								TrickerUtils.showToast(getActivity(), "多条数据不允许删除！");
								Cursor c = adapter3.getCursor();
								c.moveToPosition(position);
								int saleId = c.getInt(c.getColumnIndex("_id"));
								TrickerDB.getInstance(getActivity()).deleteSale(saleId);
								refreshView(null,Constant.SALE,isShowDetail);
							}
						} else {
//							TrickerUtils.showToast(getActivity(), "Are You Kidding Me??");
							MainActivity activity = (MainActivity)getActivity();
							TrickerUtils.showSnackbar(activity.getFab(),"R U Kidding Me?",
									Snackbar.LENGTH_SHORT);
						}
					}
				});
				builder.show();

			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
		return true;
	}

	public void refreshView() {
		refreshView("",Constant.RENT);

	}
	public void refreshView(int type) {
		refreshView("",type);

	}
	private void refreshView(String condition) {
		refreshView(condition,Constant.RENT);
	}
	private void refreshView(String condition, int type, boolean isShowDetail, boolean isShowDay) {
		if(type ==Constant.RENT){
			Cursor cursor =TrickerDB.getInstance(getActivity()).loadProjects(condition);
			setCursor(cursor);
			setListAdapter(adapter);
			adapter.swapCursor(cursor);
			updateTitle(cursor);
		}else if(type==Constant.MARRY){
			setListAdapter(adapter2);
			Cursor cursor =TrickerDB.getInstance(getActivity()).loadMarries(condition);
			adapter2.swapCursor(cursor);
			updateTitle(cursor,Constant.MARRY);
		}else if(type ==Constant.SALE){
			setListAdapter(adapter3);
			Cursor cursor =TrickerDB.getInstance(getActivity()).loadSales(condition,isShowDetail,isShowDay);
			adapter3.swapCursor(cursor);
			updateTitle(cursor,Constant.SALE);
		}
	}
	private void refreshView(String condition, int type, boolean isShowDetail) {
		refreshView(condition,type,isShowDetail,false);
	}
	private void refreshView(String condition, int type) {
		refreshView(condition,type,false);
	}

	/*
	 * 处理提示，以及变色，已有官方属性 hint替代
	 */
	/*
	 * @Override public void onClick(View v) { String data =
	 * editQuery.getText().toString(); if (!TextUtils.isEmpty(data) &&
	 * data.equals("查询")) { editQuery.setText("");
	 * editQuery.setTextColor(Color.BLACK); } }
	 */

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// 用户点击回车键，并且是弹起操作！
//		TrickerUtils.showToast(getActivity(),"Query");
		if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
			execQuery(type,isShowDetail);

		}
		return false;//设置成true，点击键盘上的   × 会无效
	}

	/**
	 * Sale 按照天还是天&类型合计
	 * @param isShowDay
     */
	private void execQuery(int type,boolean isShowDetail,boolean isShowDay) {
		this.type = type;
		String condition = "";
		String data = editQuery.getText().toString();
		if(type ==Constant.RENT){
			String strSymbol = spSymbol.getSelectedItem().toString();
			if (strSymbol == null || strSymbol.equals("")) {// like
				condition = " where project like '%" + data + "%'  or state like '%" + data + "%' or date like '%"
						+ data + "%'";
			} else if (strSymbol.equals("!")) {// 非 针对时间
				condition = " where date not like '%" + data + "%'";
			} else if (strSymbol.equals(">")) {// 大于 针对时间
				condition = " where date > '" + data + "'";
			} else if (strSymbol.equals("<")) {// ＜小于 针对时间
				condition = " where date < '" + data + "'";
			} else if (strSymbol.equals("=")) {// 等于 针对时间
				condition = " where date = '" + data + "'";
			}
			refreshView(condition);
		}else if(type ==Constant.MARRY){
//			TrickerUtils.showToast(getActivity(),"份子钱查询");
			data="";//不加条件
			condition=" where name like '%"+data+"%' ";
			refreshView(condition,Constant.MARRY);
		}else if(type == Constant.SALE){
//			data="";//不加条件
			condition=" where type like '%"+data+"%' or date like '%"+data+"%' ";
			refreshView(condition,Constant.SALE,isShowDetail,isShowDay);
		}
		// 强制关闭输入法
		TrickerUtils.closeKeybord(editQuery,getActivity());
	}
	/**
	 *
	 * @param type
	 * @param isShowDetail  显示合计还是详情（现在该参数只针对Sale）
     */
	private void execQuery(int type,boolean isShowDetail) {
		execQuery(type,isShowDetail,false);
	}
	/**
	 * 房租or份子钱
	 * @param type
     */
	private void execQuery(int type) {
		execQuery(type,false);
	}
	private void execQuery() {
		execQuery(Constant.RENT);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(parent.getId()==R.id.editType){
//			isShowDetail=true;
			if(position==0){
				setWidgetVisible(position);
				execQuery(Constant.RENT);
			}else if(position==1){
				setWidgetVisible(position);
				execQuery(Constant.MARRY);
			}else if(position==2){
				setWidgetVisible(position);
//				execQuery(Constant.SALE);
				execQuery(Constant.SALE,isShowDetail,isShowDay);
			}
		}else if(parent.getId()==R.id.spSymbol){
			switch (position) {
				case 0:
					setEditDate(false);
					break;
				case 1:
				case 2:
				case 3:
				case 4:
					setEditDate(true);
					break;

				default:
					break;
			}
		}else if(parent.getId()==R.id.editShowMethod){
			switch (position){
				case 0://合计（天&类型）
//					setWidgetVisible(position);
					isShowDetail=false;
					isShowDay=false;
					execQuery(Constant.SALE);
					break;
				case 1://合计（天）
//					setWidgetVisible(position);
					isShowDetail=false;
					isShowDay=true;
					execQuery(Constant.SALE,false,true);
					break;
				case 2://详情
					isShowDetail=true;
					isShowDay=false;
					execQuery(Constant.SALE,true);
					break;
			}
		}
	}

	private void setWidgetVisible(int type) {
		if(type==Constant.RENT){
			spSymbol.setVisibility(View.VISIBLE);
			editQuery.setVisibility(View.VISIBLE);
//			editShowMethod.setVisibility(View.GONE);
			saleLayout.setVisibility(View.GONE);
//			btnCount.setVisibility(View.VISIBLE);//统计和一键设置只有租金有
//			btnOneKeySet.setVisibility(View.VISIBLE);
			rentLayout.setVisibility(View.VISIBLE);
		}else if(type==Constant.MARRY){
			spSymbol.setVisibility(View.GONE);
			editQuery.setVisibility(View.GONE);
//			editShowMethod.setVisibility(View.GONE);
			saleLayout.setVisibility(View.GONE);
//			btnCount.setVisibility(View.GONE);
//			btnOneKeySet.setVisibility(View.GONE);
			rentLayout.setVisibility(View.GONE);
		}else if(type==Constant.SALE){
			spSymbol.setVisibility(View.GONE);
			editQuery.setVisibility(View.VISIBLE);
//			editShowMethod.setVisibility(View.VISIBLE);//显示模式（合计还是详情，只有Sale才处理）
			saleLayout.setVisibility(View.VISIBLE);
//			btnCount.setVisibility(View.GONE);
//			btnOneKeySet.setVisibility(View.GONE);
			rentLayout.setVisibility(View.GONE);
		}

	}

	private void setEditDate(boolean isDate) {
		if (isDate) {
			editQuery.setText(TrickerUtils.getSystemDate());
			editQuery.setFocusable(false);
			editQuery.setFocusableInTouchMode(false);
		} else {
			editQuery.setFocusableInTouchMode(true);
			editQuery.setFocusable(true);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.editQuery:
				selectDate(v);
				break;
			case R.id.btnCount:
				countMoney();
				break;
			case R.id.btnOneKeySet:
				oneKeySet();
				break;
			case R.id.btnAverageMoney:
				getMoneyAndDate(Constant.AVERAGE);
				break;
			case R.id.btnMaxMoney:
				getMoneyAndDate(Constant.MAX);
				break;
			case R.id.btnMinMoney:
				getMoneyAndDate(Constant.MIN);
				break;
			default:
				break;
		}
	}

	/**
	 *获取Sale日均或者最大或者最小
	 * @param type
     */
	private void getMoneyAndDate(String type) {
		String result=TrickerDB.getInstance(getActivity()).getMoneyAndDate(type);
//		TrickerUtils.showToast(getActivity(),result);
		MainActivity activity = (MainActivity)getActivity();
		TrickerUtils.showSnackbar(activity.getFab(),result,
				Snackbar.LENGTH_SHORT);
	}

	private void oneKeySet() {
		final Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("提示");
		builder.setMessage("真的要设置吗？\n该操作不可恢复！！！");
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final EditText password = new EditText(getActivity());
				// password.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
				builder.setTitle("请输入密码");
				builder.setMessage(null);
				builder.setView(password);
				builder.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
						int minute = Calendar.getInstance().get(Calendar.MINUTE);
						int total = hour - minute;
						if (password.getText() != null
								&& password.getText().toString().equals(String.valueOf(total))) {
								Cursor c = getCursor();
								String ids = "";
								for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
									String state = c.getString(c.getColumnIndex("state"));
									// 只有未结算才执行更新操作，避免本来就是已结算还去更新，浪费资源
									if (!TextUtils.isEmpty(state) && state.equals("未结算")) {
										String _id = c.getString(c.getColumnIndex("_id"));
										ids += _id + ",";
									}
								}
								ids += "-999";// 避免多一个都好，又不想subStr
								TrickerDB.getInstance(getActivity()).updateProject(ids);
								refreshView();


						} else {
//							TrickerUtils.showToast(getActivity(), "Are You Kidding Me??");
							MainActivity activity = (MainActivity)getActivity();
							TrickerUtils.showSnackbar(activity.getFab(),"R U Kidding Me?",
									Snackbar.LENGTH_SHORT);
						}
					}
				});
				builder.show();

			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	private void countMoney() {
		Cursor c = getCursor();
		BigDecimal costMoney = new BigDecimal(0);
		BigDecimal cost2Money = new BigDecimal(0);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String strMoney = c.getString(c.getColumnIndex("money"));
			BigDecimal eJMoney = TrickerUtils.parseToDecimal(strMoney);
			BigDecimal myMoney = TrickerUtils.parseToDecimal(strMoney);
			String percent = c.getString(c.getColumnIndex("percent"));
			if (percent.equals("1/2")) {
				myMoney = myMoney.multiply(Constant.HALF);
				eJMoney = eJMoney.multiply(Constant.HALF);
			} else if (percent.equals("2/3")) {
				myMoney = myMoney.multiply(Constant.TWO_PART);
				eJMoney = eJMoney.multiply(Constant.ONE_THIRD);
			}
			costMoney = costMoney.add(myMoney);
			costMoney = costMoney.setScale(2, RoundingMode.HALF_UP);
			costMoney = TrickerUtils.setScale(costMoney, 2, RoundingMode.HALF_UP);
			cost2Money = cost2Money.add(eJMoney);
			cost2Money = TrickerUtils.setScale(cost2Money, 2, RoundingMode.HALF_UP);
		}
//		TrickerUtils.showToast(getActivity(), "我共计获得金额：" + costMoney + "\n 二姐共获得金额：" + cost2Money,
//				Toast.LENGTH_LONG);
		MainActivity activity = (MainActivity)getActivity();
		TrickerUtils.showSnackbar(activity.getFab(),"我共计获得金额：" + costMoney + "\n 二姐共获得金额：" + cost2Money,
				Snackbar.LENGTH_LONG);
	}

	private void selectDate(View view) {
		if(!editQuery.isFocusableInTouchMode()){//弹出日期选择器
			OnDateSetListener callBack = new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					String date = TrickerUtils.getDateFormat().format(new Date(view.getCalendarView().getDate()));
					editQuery.setText(date);
					execQuery();
				}
			};
			TrickerUtils.selectDate(getActivity(), editQuery,callBack);


		}else{
			//暂时屏蔽掉搜索历史功能
//			Intent intent = new Intent(getActivity(), SearchActivity.class);
//			startActivity(intent);

		}
	}
//	private void setProjectVisible(boolean b) {
//		execQuery(b);
//		if(b){
////			tableLayoutProject.setVisibility(View.VISIBLE);
////			tableLayoutMarry.setVisibility(View.GONE);
//		}else{
////			tableLayoutProject.setVisibility(View.GONE);
////			tableLayoutMarry.setVisibility(View.VISIBLE);
//		}
//	}
}