package com.tricker.recordmoney.util;

import android.util.Log;

/**
 * 日志类，只需要修改 常量LEVEL的值，就可以控制
 * 日志的输出，这样就不用去找日志打印类，一行一行的屏蔽
 * @author Tricker
 *
 */
public class LogUtil {
	public static final int  VERBOSE=1;
	public static final int  DEBUG=2;
	public static final int  INFO=3;
	public static final int  WARN=4;
	public static final int  ERROR=5;
	public static final int  NOTHING=6;
	public static final int  LEVEL=VERBOSE;

	public static void v(String tag, String msg){
		if(LEVEL<=VERBOSE){
			Log.v(tag, msg);
		}
	}
	public static void d(String tag, String msg){
		if(LEVEL<=DEBUG){
			Log.d(tag, msg);
		}
	}
	public static void i(String tag, String msg){
		if(LEVEL<=INFO){
			Log.i(tag, msg);
		}
	}
	public static void w(String tag, String msg){
		if(LEVEL<=WARN){
			Log.w(tag, msg);
		}
	}
	public static void e(String tag, String msg){
		if(LEVEL<=ERROR){
			Log.e(tag, msg);
		}
	}

}
