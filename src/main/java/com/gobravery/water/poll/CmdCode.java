package com.gobravery.water.poll;

public class CmdCode {
	//客户端命令
	public static final String LOGIN="LOGIN";//1,0
	public static final String OPEN="OPEN";//1
	public static final String CLOSE="CLOSE";//1
	public static final String UN_WARING="UN_WARING";//1
	public static final String WARING="WARING";//1,2,3,4
	public static final String MSG="MSG";//1,2,3,4
	//服务端命令
	public static final String LOCK_CLIENT_INFO="LOCK_CLIENT_INFO";
	public static final String WEB_CLIENT_INFO="WEB_CLIENT_INFO";
	public static final String WEB_CLIENT_CLOSE="WEB_CLIENT_CLOSE";
	public static final String LOCK_CLIENT_CLOSE="LOCK_CLIENT_CLOSE";
	public static final String MIDDLE_SERVER_CLOSE="MIDDLE_SERVER_CLOSE";
	//设备类型
	public static final String WEB_CLIENT="WEB_CLIENT";
	public static final String MIDDLE_SERVER="MIDDLE_SERVER";
	public static final String LOCK_CLIENT="LOCK_CLIENT";
	public static final String MGR_CLIENT="MGR_CLIENT";//控制端
}
