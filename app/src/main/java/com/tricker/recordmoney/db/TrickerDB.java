package com.tricker.recordmoney.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.tricker.recordmoney.MyApplication;
import com.tricker.recordmoney.model.City;
import com.tricker.recordmoney.model.County;
import com.tricker.recordmoney.model.Marry;
import com.tricker.recordmoney.model.Project;
import com.tricker.recordmoney.model.Province;
import com.tricker.recordmoney.model.Sale;
import com.tricker.recordmoney.model.User;
import com.tricker.recordmoney.util.Constant;
import com.tricker.recordmoney.util.TrickerUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TrickerDB {
	public static final String DB_NAME = "db";
	public static final int VERSION=10;
	private static TrickerDB trickerDB;
	private SQLiteDatabase db;
	private Context context;
	/**
	 * 构造方法私有化
	 * @param context
	 */
	private TrickerDB(Context context){
		BaseDao  dbHelper = new BaseDao(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
		this.context = context;
	}
	/**
	 * 获取实例
	 * @param context
	 * @return
	 */
	public synchronized static TrickerDB getInstance(Context context){
		if(trickerDB==null){
			trickerDB = new TrickerDB(context);
		}
		return trickerDB;
	}
	/**
	 * 存储天气预报省份
	 * @param province
	 */
	public void saveProvice(Province province){
		if(province!=null){
			ContentValues values = new ContentValues();
			values.put("name", province.getName());
			values.put("code", province.getCode());
			db.insert("province", null, values);
		}
	}
	/**
	 * 存储天气预报城市
	 * @param city
	 */
	public void saveCity(City city){
		if(city!=null){
			ContentValues values = new ContentValues();
			values.put("name", city.getName());
			values.put("code", city.getCode());
			values.put("province_id", city.getProvinceId());
			db.insert("city", null, values);
		}
	}
	/**
	 * 存储天气预报县
	 * @param county
	 */
	public void saveCounty(County county){
		if(county!=null){
			ContentValues values = new ContentValues();
			values.put("name", county.getName());
			values.put("code", county.getCode());
			values.put("city_id", county.getCityId());
			db.insert("county", null, values);
		}
	}
	public void saveProject(Project project){
		saveProject(project, false);
	}

	/**
	 * 保存及修改Project
	 * @param project
	 * @param isEdit
	 */
	public void saveProject(Project project,boolean isEdit){
		if(project!=null){
			ContentValues values = new ContentValues();
			values.put("project", project.getProject());
			values.put("money", project.getMoney());
			values.put("date", project.getDate());
			values.put("percent", project.getPercent());
			values.put("remark", project.getRemark());
			values.put("state", project.getState());
			values.put("user",MyApplication.getUser().getName());
			if(isEdit){
				db.update(BaseDao.TABLE_PROJECT, values, " _id=?", new String[] { "" + project.getId() });
			}else{
				db.insert(BaseDao.TABLE_PROJECT, null, values);
			}
			//无论修改和添加都需要备份当前的最新数据库
			File fromFile = new File(TrickerUtils.getPath(context, TrickerUtils.DATABASE_PATH));
			File toFile = new File(Environment.getExternalStorageDirectory().getPath()+ File.separator+"tricker"+ File.separator+"tricker.db");
			String result =TrickerUtils.copyfile(fromFile, toFile , true);
		}
	}
	public void saveMarry(Marry marry){
		saveMarry(marry, false);
	}

	/**
	 * 保存及修改Marry
	 * @param marry
	 * @param isEdit
	 */
	public void saveMarry(Marry marry,boolean isEdit){
		if(marry!=null){
			ContentValues values = new ContentValues();
			values.put("name", marry.getName());
			values.put("getMoney", marry.getGetMoney());
			values.put("payMoney", marry.getPayMoney());
			values.put("remark", marry.getRemark());
			values.put("state", marry.getState());
			values.put("user", MyApplication.getUser().getName());
			if(isEdit){
				db.update(BaseDao.TABLE_MARRY, values, " _id=?", new String[] { "" + marry.getId() });
			}else{
				db.insert(BaseDao.TABLE_MARRY, null, values);
			}
			//无论修改和添加都需要备份当前的最新数据库
			File fromFile = new File(TrickerUtils.getPath(context, TrickerUtils.DATABASE_PATH));
			File toFile = new File(Environment.getExternalStorageDirectory().getPath()+ File.separator+"tricker"+ File.separator+"tricker.db");
			String result =TrickerUtils.copyfile(fromFile, toFile , true);
		}
	}
	public void saveSale(Sale sale){
		saveSale(sale, false);
	}

	/**
	 * 保存及修改Sale
	 * @param sale
	 * @param isEdit
	 */
	public void saveSale(Sale sale,boolean isEdit){
		if(sale!=null){
			ContentValues values = new ContentValues();
			values.put("money", sale.getMoney());
			values.put("type", sale.getType());
			values.put("week", sale.getWeek());
			values.put("remark", sale.getRemark());
			values.put("date", sale.getDate());
			values.put("user",MyApplication.getUser().getName());
			if(isEdit){
				db.update(BaseDao.TABLE_SALE, values, " _id=?", new String[] { "" + sale.getId() });
			}else{
				db.insert(BaseDao.TABLE_SALE, null, values);
			}
			//无论修改和添加都需要备份当前的最新数据库
			File fromFile = new File(TrickerUtils.getPath(context, TrickerUtils.DATABASE_PATH));
			File toFile = new File(Environment.getExternalStorageDirectory().getPath()+ File.separator+"tricker"+ File.separator+"tricker.db");
			String result =TrickerUtils.copyfile(fromFile, toFile , true);
		}
	}
	public void saveUser(User user){
		if(user!=null){
			ContentValues values = new ContentValues();
			values.put("name",user.getName());
			values.put("pwd", user.getPwd());
			db.insert(BaseDao.TABLE_USER, null, values);
			//无论修改和添加都需要备份当前的最新数据库
			File fromFile = new File(TrickerUtils.getPath(context, TrickerUtils.DATABASE_PATH));
			File toFile = new File(Environment.getExternalStorageDirectory().getPath()+ File.separator+"tricker"+ File.separator+"tricker.db");
			String result =TrickerUtils.copyfile(fromFile, toFile , true);
		}
	}
	public User findUserByName(String name){
		User user =null;
		Cursor cursor =db.rawQuery("select pwd from user where name ='"+name+"'",null);
		if(cursor!=null&&cursor.getCount()>0){
			if(cursor.moveToFirst()){
				user = new User(name,cursor.getString(cursor.getColumnIndex("pwd")));
			}

		}
		if(cursor!=null){
			cursor.close();
		}
		return  user;
	}
	public void deleteProject(int projectId){
		String[] condition = new String[] { projectId + "" };
		db.delete(BaseDao.TABLE_PROJECT, "_id=?", condition);
		File fromFile = new File(TrickerUtils.getPath(this.context, TrickerUtils.DATABASE_PATH));
		File toFile = new File(Environment.getExternalStorageDirectory().getPath()+ File.separator+"tricker"+ File.separator+"tricker.db");
		String result =TrickerUtils.copyfile(fromFile, toFile , true);
	}
	public void deleteMarry(int marryId){
		String[] condition = new String[] { marryId + "" };
		db.delete(BaseDao.TABLE_MARRY, "_id=?", condition);
		File fromFile = new File(TrickerUtils.getPath(this.context, TrickerUtils.DATABASE_PATH));
		File toFile = new File(Environment.getExternalStorageDirectory().getPath()+ File.separator+"tricker"+ File.separator+"tricker.db");
		String result =TrickerUtils.copyfile(fromFile, toFile , true);
	}
	public void deleteSale(int saleId){
		String[] condition = new String[] { saleId + "" };
		db.delete(BaseDao.TABLE_SALE, "_id=?", condition);
		File fromFile = new File(TrickerUtils.getPath(this.context, TrickerUtils.DATABASE_PATH));
		File toFile = new File(Environment.getExternalStorageDirectory().getPath()+ File.separator+"tricker"+ File.separator+"tricker.db");
		String result =TrickerUtils.copyfile(fromFile, toFile , true);
	}

	/**
	 * 根据id一键设置已结算
	 * @param ids
     */
	public void updateProject(String ids){
		db.execSQL("update " + BaseDao.TABLE_PROJECT + " set state='已结算' where _id in(" + ids + ")");
	}
	/*public List<Project> loadProjects(){
		List<Project> list = new ArrayList<Project>();
//		Cursor cursor = db.query(BaseDao.TABLE_PROJECT, null,null,null,null,null,null);
		String sql = BaseDao.QUERY_ALL + " order by date desc";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.moveToFirst()){
			do {
				Project project = new Project();
				project.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				project.setProject(cursor.getString(cursor.getColumnIndex("project")));
				project.setMoney(cursor.getString(cursor.getColumnIndex("money")));
				project.setDate(cursor.getString(cursor.getColumnIndex("date")));
				project.setPercent(cursor.getString(cursor.getColumnIndex("percent")));
				project.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
				project.setState(cursor.getString(cursor.getColumnIndex("state")));
				list.add(project);
			} while (cursor.moveToNext());

		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}*/
	public Cursor loadProjects(){
		return loadProjects("");
	}
	public Cursor loadMarries(){
		return loadMarries("");
	}
	public Cursor loadSales(){
		return loadSales("");
	}
	public Cursor loadProjects(String condition){
		if(condition==null||condition.equals("")){
			condition+=" where 1=1 ";
		}
		condition+=" and user='"+ MyApplication.getUser().getName()+"'";
		String sql = BaseDao.QUERY_ALL_PROJECT + condition+ "  order by date desc";
		return db.rawQuery(sql, null);
	}
	public Cursor loadMarries(String condition){
		if(condition==null||condition.equals("")){
			condition+=" where 1=1 ";
		}
		condition+=" and user='"+ MyApplication.getUser().getName()+"'";
		String sql = BaseDao.QUERY_ALL_MARRY + condition+ "  order by getMoney desc";
		return db.rawQuery(sql, null);
	}
	public Cursor loadSales(String condition){
		return loadSales(condition,false,false);
	}
	public Cursor loadSales(String condition, boolean isShowDetail, boolean isShowDay){
		if(condition==null||condition.equals("")){
			condition+=" where 1=1 ";
		}
		condition+=" and user='"+ MyApplication.getUser().getName()+"'";
//		String sql = BaseDao.QUERY_ALL_SALE + condition+ "  order by date desc";
		//把日期和类型相同的进行合并处理
		String sql = "select _id,substr(date,1,10) date,week,sum(money) money,remark,type,user,count(money) count from SALE" + condition+ " group by type,substr(date,1,10) order by date desc";
		if(isShowDetail){
			sql="select _id,date,week,money,remark,type,user,1 count from SALE" + condition+ "  order by date desc";
		}else if(isShowDay){
			sql = "select _id,substr(date,1,10) date, week,sum(money) money,remark,'合计' type,user,'' count from SALE" + condition+ " group by substr(date,1,10) order by date desc";
		}
		return db.rawQuery(sql, null);
	}
	public String getMoneyAndDate(String type){
		String result ="暂无数据！";
		String sql ="";
		if(type.equals(Constant.AVERAGE)){
			sql ="select avg(saleInfo.money) money,count(1) count from (select sum(money) money from SALE  group by substr(date,1,10)) saleInfo";
		}else if(type.equals(Constant.MAX)){
			sql="select sum(money) money,substr(date,1,10) date,week from SALE  group by substr(date,1,10) order by sum(money) desc";
		}else if(type.equals(Constant.MIN)){
			sql="select sum(money) money,substr(date,1,10) date,week from SALE  group by substr(date,1,10) order by sum(money) asc";
		}
		Cursor cursor=db.rawQuery(sql,null);
		if(cursor!=null&&cursor.getCount()>0){
			cursor.moveToFirst();
			String money =cursor.getString(cursor.getColumnIndex("money"));
			if(type==Constant.AVERAGE){
				int count =cursor.getInt(cursor.getColumnIndex("count"));
				if(count!=0){
					result=count+" 天平均："+"￥"+money;
				}
			}else{
				String date =cursor.getString(cursor.getColumnIndex("date"));
				String week =cursor.getString(cursor.getColumnIndex("week"));
				result="￥"+money+"\n"+date+"\t"+week;
			}
		}
		if(cursor!=null&&!cursor.isClosed()){
			cursor.close();
		}
		return result;
	}
	public String getSaleInfo(String date, String type){
		String result =type+":\n";
		String sql="select money,date from SALE where type like '%"+type+"%' and date like '%"+date+"%' order by money asc";
		Cursor cursor = db.rawQuery(sql,null);
		Map<Double,Integer> map= new TreeMap<Double,Integer>();
		if(cursor.moveToFirst()){
			do {
				String strMoney =cursor.getString(cursor.getColumnIndex("money"));
				double money = Double.parseDouble(strMoney);
				if(!map.containsKey(money)){
					map.put(money,1);
				}else{
					Integer count =map.get(money);
					map.put(money,++count);
				}
			} while (cursor.moveToNext());

		}
		map = sortMapByKey(map);//对map按照key排序
		for (Double money :map.keySet()){
			result +="\t\t￥"+money+"\t\t"+"数量："+map.get(money)+"\n";
		}
		if(cursor!=null&&!cursor.isClosed()){
			cursor.close();
		}
		return  result;
	}
	/**
	 * 使用 Map按key进行排序
	 * @param map
	 * @return
	 */
	public Map<Double, Integer> sortMapByKey(Map<Double, Integer> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<Double, Integer> sortMap = new TreeMap<Double, Integer>(
				new MapKeyComparator());

		sortMap.putAll(map);

		return sortMap;
	}
	class MapKeyComparator implements Comparator<Double> {
		@Override
		public int compare(Double num1, Double num2) {
			return num1.compareTo(num2);
		}
	}
	/**
	 * 获取省份
	 * @return
	 */
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("province", null,null,null,null,null,null);
		if(cursor.moveToFirst()){
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				province.setName(cursor.getString(cursor.getColumnIndex("name")));
				province.setCode(cursor.getString(cursor.getColumnIndex("code")));
				list.add(province);
			} while (cursor.moveToNext());

		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	/**
	 * 根据省份获取所有城市
	 * @param provinceId
	 * @return
	 */
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("city", null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
		if(cursor.moveToFirst()){
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				city.setName(cursor.getString(cursor.getColumnIndex("name")));
				city.setCode(cursor.getString(cursor.getColumnIndex("code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());

		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	/**
	 * 根据城市获取所有的县
	 * @param cityId
	 * @return
	 */
	public List<County> loadCounties(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("county", null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
		if(cursor.moveToFirst()){
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				county.setName(cursor.getString(cursor.getColumnIndex("name")));
				county.setCode(cursor.getString(cursor.getColumnIndex("code")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());

		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	public void execSQL(String sql){
		db.execSQL(sql);
	}
}
