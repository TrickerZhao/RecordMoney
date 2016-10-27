package com.tricker.recordmoney.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public synchronized static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestProperty("Charset", "utf-8");
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
//					connection.connect();
					InputStream in = connection.getInputStream();

					BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					/*
					 * byte[] buf = new byte[1024]; StringBuilder response = new
					 * StringBuilder(); while((in.read(buf))!=-1){
					 * response.append(new String(buf)); buf=new
					 * byte[1024];//重新生成，避免和上次读取的数据重复 }
					 */

					if (listener != null) {
						listener.onFinish(response.toString());
					}
					// reader.close();
					// in.close();
				} catch (Exception e) {
					e.printStackTrace();
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}

				}
			}
		}).start();
	}
}
