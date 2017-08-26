package com.gobravery.water.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class PropertisUitls {
	//监控服务器请求地址
	private String url ;
	//用户代码所在作用域(session或request)
	private String scope ;
	//用户代码
	private String userCode;
	//用户名
	private String userName ;
	//项目名
	private String saveFilePath;
//	#是其类型,1=是，0=否
	private boolean isObjectType=false;
//	#其他类型类的全路径名称
	private String className;
//	#用户代码方法
	private String codeMethod;
//	#用户名方法
	private String nameMethod;
	private static PropertisUitls properties;
	
	private PropertisUitls() throws Exception
	{
		InputStream in = this.getClass().getResourceAsStream("/config.properties");
		Properties pro = new Properties();
		pro.load(in);
		url = pro.getProperty("url");
		scope = pro.getProperty("scope");
		userCode = pro.getProperty("userCode");
		userName = pro.getProperty("userName");
		saveFilePath = pro.getProperty("saveFilePath");
		String objType= pro.getProperty("isObjectType");
		if(objType!=null && objType.trim().equals("1")){
			isObjectType = true;
		}
		
		className = pro.getProperty("className");
		
		codeMethod = pro.getProperty("codeMethod");
		
		nameMethod = pro.getProperty("nameMethod");
		in.close();
	}
	
	public String getSaveFilePath() {
		return saveFilePath;
	}

	public static PropertisUitls getInstance() throws Exception
	{
		if(properties == null)
		{
			properties = getSingle();
		}
		return properties;
	}
	
	private synchronized static PropertisUitls getSingle() throws Exception
	{
		if(properties == null)
		{
			properties = new PropertisUitls();
		}
		return properties ;
	}

	public String getUrl() {
		return url;
	}

	public String getScope() {
		return scope;
	}

//	public String getProName() {
//		if(proName!=null){
//			try {
//				proName=new String(proName.getBytes("ISO8859-1"),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				proName="";
//				System.out.println("不能转换proName");
//			}
//		}
//		return proName;
//	}

	public String getUserCode() {
		return userCode;
	}

	public boolean getIsObjectType() {
		return isObjectType;
	}

	public String getClassName() {
		return className;
	}

	public String getCodeMethod() {
		return codeMethod;
	}

	public String getNameMethod() {
		return nameMethod;
	}

	public String getUserName() {
		return userName;
	}
	
}
