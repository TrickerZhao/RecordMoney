package com.tricker.recordmoney;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tricker.recordmoney.service.AutoUpdateService;
import com.tricker.recordmoney.util.HttpCallbackListener;
import com.tricker.recordmoney.util.HttpUtil;
import com.tricker.recordmoney.util.TrickerUtils;
import com.tricker.recordmoney.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText,publishText,weatherDespText,
			tempText,temp1Text,temp2Text,currentDateText,txtSunrise,txtSunset;
	private Button switchCity,refreshWeather;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);
		findViews();
		String city_name =getIntent().getStringExtra("city_name");
		if(!TextUtils.isEmpty(city_name)){
			publishText.setText("同步中。。。");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(city_name);
		}else{
			showWeather();
		}
	}



	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		txtSunrise.setText(prefs.getString("sunrise", ""));
		txtSunset.setText(prefs.getString("sunset", ""));
		tempText.setText(prefs.getString("current_temp", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		currentDateText.setText(prefs.getString("current_date", ""));
		publishText.setText("更新于 "+prefs.getString("publish_time", ""));

		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);

		Intent intent = new Intent(this,AutoUpdateService.class);
		startService(intent);
	}
	private void queryWeatherCode(String countyCode) {
		try {
			countyCode = URLEncoder.encode(countyCode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
//		String address="http://wthrcdn.etouch.cn/weather_mini?city="+countyCode;
		String address="http://wthrcdn.etouch.cn/WeatherApi?city="+countyCode;

		queryFromServer(address,"countyCode");
	}
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponse(WeatherActivity.this, response);
				runOnUiThread(new Runnable() {
					public void run() {
						showWeather();
					}
				});
			}

			@Override
			public void onError(final Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						TrickerUtils.showToast(WeatherActivity.this, e.getMessage());
						publishText.setText("同步失败！");
					}
				});
			}
		});
	}
	/*private void queryWeatherCode(String countyCode) {
		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	private void queryWeatherInfo(String weatherCode) {
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
//		LogUtil.e("tricker", address);
		queryFromServer(address,"weatherCode");
	}

	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String[] array = response.split("\\|");
						if(array!=null&&array.length==2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
//					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						public void run() {
							showWeather();
						}
					});
				}
			}

			@Override
			public void onError(final Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						TrickerUtils.showToast(WeatherActivity.this, e.getMessage());
						publishText.setText("同步失败！");
					}
				});
			}
		});
	}*/





	private void findViews() {
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		tempText = (TextView) findViewById(R.id.temp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		txtSunrise = (TextView) findViewById(R.id.sunrise);
		txtSunset = (TextView) findViewById(R.id.sunset);

		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.switch_city){
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather", true);
			startActivity(intent);
			finish();
		}else if(v.getId()==R.id.refresh_weather){
			publishText.setText("同步中。。。");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String cityName = prefs.getString("city_name", "");
//			cityName="上海";
			if(!TextUtils.isEmpty(cityName)){
				queryWeatherCode(cityName);
			}
		}
	}
}
