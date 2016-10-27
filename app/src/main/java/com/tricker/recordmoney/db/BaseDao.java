package com.tricker.recordmoney.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DB类
 */
public class BaseDao extends SQLiteOpenHelper {
	/*项目表*/
	public static final String TABLE_PROJECT = "PROJECT",TABLE_USER="USER",TABLE_MARRY="MARRY",TABLE_SALE="SALE";
	/*查询所有的项目*/
	public static final String QUERY_ALL_PROJECT = "select * from " + BaseDao.TABLE_PROJECT;
	public static final String QUERY_ALL_MARRY = "select * from " + BaseDao.TABLE_MARRY;
	public static final String QUERY_ALL_SALE = "select * from " + BaseDao.TABLE_SALE;
	/*创建项目表*/
	public static final String CREATE_PROJECT = "create table project (_id integer primary key autoincrement,"
			+ "project text default 0,money int default 0,date date,percent real,remark text  default \"备注\","
			+ "state text default \"未结算\",user text default \"Tricker\")";
	/*创建省份表*/
	public static final String CREATE_PROVINCE="create table province(_id integer primary key autoincrement,"
			+ "name text,code text)";
	/*创建城市表*/
	public static final String CREATE_CITY="create table city(_id integer primary key autoincrement,"
			+ "name text,code text,province_id integer)";
	/*创建县/区表*/
	public static final String CREATE_COUNTY="create table county(_id integer primary key autoincrement,"
			+ "name text,code text,city_id integer)";
	/*创建历史记录表*/
	public static final String CREATE_HISTORY = "create table history (_id integer primary key autoincrement, h_name text not null)";
	/*创建用户表*/
	public static final String CREATE_USER = "create table user (_id integer primary key autoincrement, name text not null, pwd text not null)";
	/*创建份子钱表*/
	public static final String CREATE_MARRY = "create table marry (_id integer primary key autoincrement, name text not null, getMoney int default 0" +
			", payMoney int default 0,remark text,state text default \"未结算\",user text default \"Tricker\")";
	/*创建销售表*/
	public static final String CREATE_SALE = "create table sale (_id integer primary key autoincrement, date text not null, week text not null" +
			", money int default 0,remark text,type text,user text default \"Tricker\")";
	public BaseDao(Context context) {
		super(context, "db", null, TrickerDB.VERSION);
	}
	public BaseDao(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROJECT);
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
		db.execSQL(CREATE_HISTORY);
		db.execSQL(CREATE_USER);
		db.execSQL(CREATE_MARRY);
		db.execSQL(CREATE_SALE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//版本升级，每个版本修改的  升级 sql，不加break，避免跳版本升级出现问题
		switch (oldVersion) {//由于软件升级从1开始
			case 1:
			case 2:
			default:
				break;
		}

	}

}
