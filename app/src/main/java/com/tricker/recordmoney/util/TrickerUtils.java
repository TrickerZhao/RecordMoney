package com.tricker.recordmoney.util;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.tricker.recordmoney.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class TrickerUtils {
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
	public static final int PACKAGE_RESOURCE_PATH = 0;
	public static final int FILES_DIR = 1;
	public static final int FILES_DIR_ABSOLUTE_PATH = 2;
	public static final int DATABASE_PATH = 3;

	/**
	 * 字符串转换成BigDecimal
	 *
	 * @param num
	 * @return
	 */
	public static BigDecimal parseToDecimal(String num) {
		return new BigDecimal(num);
	}

	/**
	 * 设置BigDecimal 精度
	 *
	 * @param num
	 * @param newScale
	 * @param roundingMode
	 * @return
	 */
	public static BigDecimal setScale(BigDecimal num, int newScale, RoundingMode roundingMode) {
		return num.setScale(newScale, roundingMode);
	}

	/**
	 * Toast
	 *
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToast(Context context, String text, int duration) {
		if (TextUtils.isEmpty(text)) {
			text = "nothing";
		}
		Toast.makeText(context, text, duration).show();
		;
	}

	public static void showToast(Context context, String text) {
		showToast(context,text, Toast.LENGTH_SHORT);
	}
	public static void showSnackbar(View view, String text, int duration) {
		if (TextUtils.isEmpty(text)) {
			text = "nothing";
		}
		Snackbar.make(view,text,duration).show();
	}
	public static void showSnackbar(View view, String text) {
		showSnackbar(view,text, Snackbar.LENGTH_SHORT);
	}


	/**
	 * 选择日期
	 * @param context
	 * @param editDate
     */
	public static void selectDate(Context context, final EditText editDate) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		String data = editDate.getText().toString();
		if (!TextUtils.isEmpty(data)) {
			String strs[] = data.split("/");
			year = Integer.parseInt(strs[0]);
			month = Integer.parseInt(strs[1]) - 1;
			day = Integer.parseInt(strs[2]);
		}
		OnDateSetListener callBack = new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//				String date = format.format(new Date(view.getCalendarView().getDate()));
				String date = format.format(new Date(year-1900,monthOfYear,dayOfMonth));
				editDate.setText(date);
			}
		};
		DatePickerDialog dlg = new DatePickerDialog(context, callBack, year, month, day);
		dlg.show();
	}
	/*public static void selectDateTime(Context context, final EditText editDate) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		String data = editDate.getText().toString();
		if (!TextUtils.isEmpty(data)) {
			String strs[] = data.split("/");
			year = Integer.parseInt(strs[0]);
			month = Integer.parseInt(strs[1]) - 1;
			day = Integer.parseInt(strs[2]);
		}
//		OnDateSetListener callBack = new OnDateSetListener() {
//			@Override
//			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//				String date = format.format(new Date(view.getCalendarView().getDate()));
//				editDate.setText(date);
//			}
//		};
		TimePickerDialog.OnTimeSetListener callBack = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			}
		};
//		DatePickerDialog dlg = new DatePickerDialog(context, callBack, year, month, day);
		TimePickerDialog dlg = new TimePickerDialog(context,callBack,12,55,true);
		dlg.show();
	}*/

	public static void selectDate(Context context, final EditText editDate, OnDateSetListener callBack) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		String data = editDate.getText().toString();
		if (!TextUtils.isEmpty(data)) {
			String strs[] = data.split("/");
			year = Integer.parseInt(strs[0]);
			month = Integer.parseInt(strs[1]) - 1;
			day = Integer.parseInt(strs[2]);
		}
		/*
		 * OnDateSetListener callBack = new OnDateSetListener() {
		 *
		 * @Override public void onDateSet(DatePicker view, int year, int
		 * monthOfYear, int dayOfMonth) { String date = format.format(new
		 * Date(view.getCalendarView().getDate())); editDate.setText(date); } };
		 */
		DatePickerDialog dlg = new DatePickerDialog(context, callBack, year, month, day);
		dlg.show();
	}

	public static String getPath(Context context, int path) {
		switch (path) {
			case PACKAGE_RESOURCE_PATH:
				return context.getPackageResourcePath();
			case FILES_DIR:
				return context.getFilesDir().toString();
			case FILES_DIR_ABSOLUTE_PATH:
				return context.getFilesDir().getAbsolutePath();
			case DATABASE_PATH:
				return context.getDatabasePath("db").getAbsolutePath();
			default:
				return null;
		}
	}

//	public static String copyDB(Context context) {
//		String result = "success";
//		return result;
//	}
//
//	public static String coverDB(Context context) {
//		String result = "";
//		return result;
//	}

	public static String copyfile(File fromFile, File toFile, Boolean rewrite) {
//		showToast(MyApplication.getContext(),fromFile.toString());
//		showToast(MyApplication.getContext(),toFile.toString());
		String result="success";
		if (!fromFile.exists()) {
			result="复制的文件不存在";
			return result;
		}

		if (!fromFile.isFile()) {
			result="复制的不是文件";
			return result;
		}

		if (!fromFile.canRead()) {
			result="复制的文件不可读";
			return result;
		}

		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}

		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
		// 当文件不存时，canWrite一直返回的都是false

		// if (!toFile.canWrite()) {

		// MessageDialog.openError(new Shell(),"错误信息","不能够写将要复制的目标文件" +
//		 toFile.getPath());

		// Toast.makeText(this,"不能够写将要复制的目标文件", Toast.LENGTH_SHORT);

		// return ;

		// }
		java.io.FileInputStream fosfrom=null;
		java.io.FileOutputStream fosto=null;
		try {

			fosfrom = new java.io.FileInputStream(fromFile);
			fosto = new FileOutputStream(toFile);

			byte bt[] = new byte[1024];

			int c;

			while ((c = fosfrom.read(bt)) > 0) {

				fosto.write(bt, 0, c); // 将内容写到新文件当中

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fosfrom!=null){
					fosfrom.close();
				}
				if(fosto!=null){
					fosto.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public static String getSystemDate() {
		String date = format.format(Calendar.getInstance().getTime());
		return date;
	}
	public static String getSystemDateTime() {
		String date = timeFormat.format(Calendar.getInstance().getTime());
		return date;
	}
	public static String getDayOfWeek(){
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
		return weekDays[week];
	}
	public static String getDayOfWeek(Date date){
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week =calendar.get(Calendar.DAY_OF_WEEK)-1;
		return weekDays[week];
	}

	public static SimpleDateFormat getDateFormat() {
		return format;
	}
	public static SimpleDateFormat getDateTimeFormat() {
		return timeFormat;
	}

	/**
	 * 打卡软键盘
	 *
	 * @param mEditText 输入框
	 * @param mContext  上下文
	 */
	public static void openKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	 *
	 * @param mEditText 输入框
	 * @param mContext  上下文
	 */
	public static void closeKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}
	public static int getItemPosition(String key){
		if(key==null||key.equals("")){
			return -1;
		}
		Map<String,Integer> values=new HashMap<>();
		values.put("堆堆袜",0);
		values.put("短袜",1);
		values.put("连裤袜",2);
		values.put("打底衫",3);
		values.put("打底裤",4);
		values.put("内裤",5);
		values.put("睡衣",6);
		values.put("内衣",7);
		values.put("帽子",8);
		values.put("围巾",9);
		values.put("背心",10);
		values.put("保暖内衣",11);
		values.put("手套",12);
		values.put("袖套",13);
		values.put("口罩",14);
		values.put("发带",15);
		values.put("其他",16);
		values.put("合计",17);
		return values.get(key);
	}

}
