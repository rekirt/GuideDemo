package com.ud.client.app_api.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Android中与电话功能相关的类是 TelephonyManager ，此类中定义了很多常量，以下分类说明
 * 获取以下信息需要在AndroidManifest.xml中指定权限
 * 
 * 一、 数据连接状态 获取数据连接状态：int getDataState() 获取数据活动状态：int getDataActivity() 常用的有这几个：
 * int DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据 int DATA_ACTIVITY_OUT 数据连接状态：活动，正在发送数据
 * int DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据 int DATA_ACTIVITY_NONE
 * 数据连接状态：活动，但无数据发送和接受 int DATA_CONNECTED 数据连接状态：已连接 int DATA_CONNECTING
 * 数据连接状态：正在连接 int DATA_DISCONNECTED 数据连接状态：断开 int DATA_SUSPENDED 数据连接状态：暂停
 * 
 * 二、 移动网络类型 获取网络类型：int getNetworkType() 常用的有这几个： int NETWORK_TYPE_CDMA
 * 网络类型为CDMA int NETWORK_TYPE_EDGE 网络类型为EDGE int NETWORK_TYPE_EVDO_0 网络类型为EVDO0
 * int NETWORK_TYPE_EVDO_A 网络类型为EVDOA int NETWORK_TYPE_GPRS 网络类型为GPRS int
 * NETWORK_TYPE_HSDPA 网络类型为HSDPA int NETWORK_TYPE_HSPA 网络类型为HSPA int
 * NETWORK_TYPE_HSUPA 网络类型为HSUPA int NETWORK_TYPE_UMTS 网络类型为UMTS
 * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
 * 
 * 三、 手机制式类型 获取手机制式：int getPhoneType() int PHONE_TYPE_CDMA 手机制式为CDMA，电信 int
 * PHONE_TYPE_GSM 手机制式为GSM，移动和联通 int PHONE_TYPE_NONE 手机制式未知
 * 
 * 四、 SIM卡状态 获取SIM卡状态：int getSimState() int SIM_STATE_ABSENT SIM卡未找到 int
 * SIM_STATE_NETWORK_LOCKED SIM卡网络被锁定，需要Network PIN解锁 int SIM_STATE_PIN_REQUIRED
 * SIM卡PIN被锁定，需要User PIN解锁 int SIM_STATE_PUK_REQUIRED SIM卡PUK被锁定，需要User PUK解锁
 * int SIM_STATE_READY SIM卡可用 int SIM_STATE_UNKNOWN SIM卡未知
 * 
 * 五、其它信息 String getSimCountryIso() 返回SIM卡提供商的国家代码 String getNetworkCountryIso()
 * 返回ISO标准的国家码，即国际长途区号 String getSimOperator() String getNetworkOperator()
 * 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI) String getSimOperatorName() String
 * getNetworkOperatorName() 返回移动网络运营商的名字(SPN) String getSubscriberId()
 * 返回IMSI，即国际移动用户识别码 String getDeviceId() 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
 * String getSimSerialNumber() 返回SIM卡的序列号(IMEI) String getLine1Number()
 * 返回手机号码，对于GSM网络来说即MSISDN boolean isNetworkRoaming() 返回手机是否处于漫游状态
 * 
 * 解释： IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
 * IMSI共有15位，其结构如下： MCC+MNC+MIN MCC：Mobile Country Code，移动国家码，共3位，中国为460;
 * MNC:Mobile NetworkCode，移动网络码，共2位 在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03
 * 合起来就是（也是Android手机中APN配置文件中的代码）： 中国移动：46000 46002 中国联通：46001 中国电信：46003
 * 举例，一个典型的IMSI号码为460030912121001
 * 
 * IMEI是International Mobile Equipment Identity （国际移动设备标识）的简称
 * IMEI由15位数字组成的”电子串号”，它与每台手机一一对应，而且该码是全世界唯一的 其组成为： 1. 前6位数(TAC)是”型号核准号码”，一般代表机型
 * 2. 接着的2位数(FAC)是”最后装配号”，一般代表产地 3. 之后的6位数(SNR)是”串号”，一般代表生产顺序号 4.
 * 最后1位数(SP)通常是”0″，为检验码，目前暂备用
 * 
 * @author lc++
 * 
 */
public class NetWorkUtil {

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gps是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = locationManager.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	/**
	 * wifi是否打开
	 */
	public static boolean isWiFiActive(Context inContext) {   
        Context context = inContext.getApplicationContext();   
        ConnectivityManager connectivity = (ConnectivityManager) context  .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (connectivity != null) {   
            NetworkInfo[] info = connectivity.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {   
                        return true;   
                    }   
                }   
            }   
        }   
        return false;   
    }   

	/**
	 * wifi是否打开
	 * 有的机型有问题
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 判断当前网络是否是wifi网络
	 * 
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isWifiNet(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否手机网络
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isPhoneNet(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			/**
			 * activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
			 * 
			 * NETWORK_TYPE_CDMA 网络类型为CDMA NETWORK_TYPE_EDGE 网络类型为EDGE
			 * NETWORK_TYPE_EVDO_0 网络类型为EVDO0 NETWORK_TYPE_EVDO_A 网络类型为EVDOA
			 * NETWORK_TYPE_GPRS 网络类型为GPRS NETWORK_TYPE_HSDPA 网络类型为HSDPA
			 * NETWORK_TYPE_HSPA 网络类型为HSPA NETWORK_TYPE_HSUPA 网络类型为HSUPA
			 * NETWORK_TYPE_UMTS 网络类型为UMTS
			 * 
			 * 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信 的3G为EVDO
			 */
			return true;
		}
		return false;
	}

	/**
	 * IP地址<br>
	 * code from:
	 * http://www.droidnova.com/get-the-ip-address-of-your-device,304.html <br>
	 * 
	 * @return 如果返回null，证明没有网络链接。 如果返回String，就是设备当前使用的IP地址，不管是WiFi还是3G
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {

			return null;
		}
		return null;
	}

	/**
	 * 获取MAC地址 <br>
	 * code from: http://orgcent.com/android-wifi-mac-ip-address/
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * IP转整型
	 * 
	 * @param ip
	 * @return
	 */
	public static long ip2int(String ip) {
		String[] items = ip.split("\\.");
		return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16
				| Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
	}

	/**
	 * 整型转IP
	 * 
	 * @param ipInt
	 * @return
	 */
	public static String int2ip(long ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	/*
	 * HACKISH: These constants aren't yet available in my API level (7), but I
	 * need to handle these cases if they come up, on newer versions
	 */
	public static final int NETWORK_TYPE_EHRPD = 14; // Level 11
	public static final int NETWORK_TYPE_EVDO_B = 12; // Level 9
	public static final int NETWORK_TYPE_HSPAP = 15; // Level 13
	public static final int NETWORK_TYPE_IDEN = 11; // Level 8
	public static final int NETWORK_TYPE_LTE = 13; // Level 11

	/**
	 * 检查是否有任何连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 检查是否有快速连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectedFast(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected() && isConnectionFast(
				info.getType(), info.getSubtype()));
	}

	/**
	 * 检查你的连接速度很快
	 * 
	 * @param type
	 * @param subType
	 * @return
	 */
	public static boolean isConnectionFast(int type, int subType) {
		if (type == ConnectivityManager.TYPE_WIFI) {
			System.out.println("CONNECTED VIA WIFI");
			return true;
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return false; // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return true; // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return true; // ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return false; // ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return true; // ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return true; // ~ 700-1700 kbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return true; // ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return true; // ~ 400-7000 kbps
				// NOT AVAILABLE YET IN API LEVEL 7
			case NETWORK_TYPE_EHRPD:
				return true; // ~ 1-2 Mbps
			case NETWORK_TYPE_EVDO_B:
				return true; // ~ 5 Mbps
			case NETWORK_TYPE_HSPAP:
				return true; // ~ 10-20 Mbps
			case NETWORK_TYPE_IDEN:
				return false; // ~25 kbps
			case NETWORK_TYPE_LTE:
				return true; // ~ 10+ Mbps
				// Unknown
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;
			default:
				return false;
			}
		} else {
			return false;
		}
	}

	// 判断网络是否可用
	public static boolean isNetworkAvailable_00(Context context) {
		ConnectivityManager cm = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE));
		if (cm != null) {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}

	// 判断网络是否可用
	public static boolean isNetworkAvailable_01(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		if (network != null) {
			return network.isAvailable();
		}
		return false;
	}

	// 更加严谨的判断网络是否可用
	public static boolean checkNet(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {

				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {

					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
