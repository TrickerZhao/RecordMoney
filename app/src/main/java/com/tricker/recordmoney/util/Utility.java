package com.tricker.recordmoney.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.tricker.recordmoney.db.TrickerDB;
import com.tricker.recordmoney.model.City;
import com.tricker.recordmoney.model.County;
import com.tricker.recordmoney.model.Province;
import com.tricker.recordmoney.model.Weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.content.ContentValues.TAG;
import static com.tricker.recordmoney.R.id.temp1;
import static com.tricker.recordmoney.R.id.temp2;
import static com.tricker.recordmoney.R.menu.weather;

/**
 * 天气工具类
 */
public class Utility {
	private static WeatherParser parser;
	public synchronized static boolean handleProvincesResponse(TrickerDB db,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
				for (String p : allProvinces) {
					String[] array = p.split("\\|");//01|北京
//					String[] array = p.split("|");
					Province province = new Province();
					province.setCode(array[0]);
					province.setName(array[1]);
					db.saveProvice(province);
				}
				return true;
			}
		}
		return false;
	}
	public synchronized static boolean handlecitiesResponse(TrickerDB db, String response, int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities!=null&&allCities.length>0){
				for (String p : allCities) {
					String[] array = p.split("\\|");
//					String[] array = p.split("|");
					City city = new City();
					city.setCode(array[0]);
					city.setName(array[1]);
					city.setProvinceId(provinceId);
					db.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	public synchronized static boolean handleCountiesResponse(TrickerDB db, String response, int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties!=null&&allCounties.length>0){
				for (String p : allCounties) {
					String[] array = p.split("\\|");
//					String[] array = p.split("|");
					County county = new County();
					county.setCode(array[0]);
					county.setName(array[1]);
					county.setCityId(cityId);
					db.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 中国天气网
	 * @param context
	 * @param response
	 */
	/*public static void handleWeatherResponse(Context context,String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weahterInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weahterInfo.getString("city");
			String weatherCode =  weahterInfo.getString("cityid");
			String temp1 =  weahterInfo.getString("temp1");
			String temp2 =  weahterInfo.getString("temp2");
			String weatherDesp = weahterInfo.getString("weather");
			String publishTime = weahterInfo.getString("ptime");
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

		}
	}*/
	public static void handleWeatherResponse(Context context, String response){
		try {
			/*JSONObject jsonObject = new JSONObject(response);
			JSONObject weahterInfo = jsonObject.getJSONObject("data");
			JSONArray week = weahterInfo.getJSONArray("forecast");
			JSONObject today = week.getJSONObject(0);
			String cityName = weahterInfo.getString("city");
//			String weatherCode =  weahterInfo.getString("cityid");
			String temp1 =  today.getString("low");
			String temp2 =  today.getString("high");
//			String weatherDesp = weahterInfo.getString("ganmao");
			String weatherDesp = today.getString("type");
//			String publishTime = weahterInfo.getString("ptime");
			saveWeatherInfo(context,cityName,"",temp1,temp2,weatherDesp,"");*/

			//xml格式
			InputStream is = new ByteArrayInputStream(response.getBytes());
			parser = new DomWeatherParser();  //SaxWeatherParser
			Weather weather = parser.parse(is);  //解析输入流
			saveWeatherInfo(context,weather);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

		}
	}
	private static void saveWeatherInfo(Context context,Weather weather) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", weather.getCity());
		editor.putString("weather_code", weather.getCity());
		editor.putString("temp1", weather.getForecasts().get(1).getLow().substring(2));
		editor.putString("temp2", weather.getForecasts().get(1).getHigh().substring(2));
		editor.putString("weather_desp", weather.getEnvironment().getSuggest());
		editor.putString("publish_time", weather.getUpdateTime());
		editor.putString("current_date", TrickerUtils.getSystemDate());
		editor.putString("current_temp",weather.getTemprature()+"");
		editor.putString("shidu",weather.getHumidity()+"");
		editor.putString("current_temp",weather.getTemprature()+"℃");
		editor.putString("sunrise","日出："+weather.getSunrise());
		editor.putString("sunset","日落："+weather.getSunset());
		editor.putString("aqi",weather.getEnvironment().getAqi()+"");
		editor.putString("pm25",weather.getEnvironment().getPm25()+"");
		editor.putString("quality",weather.getEnvironment().getQuality());
		editor.commit();
	}
}
