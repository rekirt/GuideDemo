package com.ud.client.app_api.constants;

public interface Constant {

	public static String PASSWD = "udududud";
	public static String BASEURL = "@CLIENT@";
	public static String SERVER_TEST = "http://api.udmen.com:8002/";
	public static String SERVER_BUILD = "http://api.udmen.com:8002/";

	public static int CONNECT_TIMEOUT = 3000;
	public static int READ_TIMEOUT = 3000;

	/** response message */
	String SERVICE_ERROR = "服务器连接失败";
	String RESPONSE_EMPTY = "返回数据为空";
	String RESPONSE_DATA_EMPTY = "解析后数据为空";
	String RESPONSE_MAP_EMPTY = "解析后map集合为空";
	String UDSERVER_USER = "udclient_user";
	String UDSERVER_CACHE = "udclient_cache";

	public static class Config{
		public static final boolean DEVELOPER_MODE = true;
	}

}
