package com.gobravery.water.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class URLUtils {
	/**请求地址*/
	private String requstPath ;
	
	public URLUtils(String path)
	{
		requstPath = path;
	}
	/**发送请求信息*/
	public Boolean request(Object o) throws Exception
	{
		//请求连接
		try {
			URL url = new URL(requstPath);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			setRequestConfig(urlConnection);
			urlConnection.connect();
			//请求信息及参数设定
			ObjectOutputStream oos = new ObjectOutputStream(urlConnection.getOutputStream());
			oos.writeObject(o);
			oos.flush();
			oos.close();
			urlConnection.getInputStream().close();
			//请求响应信息
			String response = response(urlConnection.getInputStream());
			urlConnection.disconnect();
		} catch (Exception e) {
		}
		return true;
	}
	/**响应信息*/
	public String response(InputStream in) throws Exception
	{
		StringBuffer response = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while((line = reader.readLine())!=null)
		{
			response.append(line);
		}
		reader.close();
		return response.toString();
	}
	/**
	 * 设置url请求参数信息
	 * @param url 请求连接信息
	 */
	public void setRequestConfig(HttpURLConnection url) throws ProtocolException
	{
		url.setDoOutput(true);  
		url.setDoInput(true);  
		url.setUseCaches(false);  
		url.setRequestProperty("Content-type","application/x-java-serialized-object");  
		url.setRequestMethod("POST");  
	}
	
}
