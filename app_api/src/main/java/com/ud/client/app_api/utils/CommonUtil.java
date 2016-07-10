package com.ud.client.app_api.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import com.ud.client.app_api.BaseApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

/**
 * 网络状态 获取RAM、ROM、SDCARD空间大小、转换帮助类
 * 
 * @author lc
 * 
 */
public class CommonUtil {

	private static final int ERROR = -1;
	private Activity mActivity;
	private StorageManager mStorageManager;
	private Method mMethodGetPaths;
	private Method mMethodGetPathsState;
	private String TAG = "StorageList";

	/**
	 * 初始化获得StorageManager
	 *
	 * @param activity
	 */
	public CommonUtil(Activity activity) {
		mActivity = activity;
		if (mActivity != null) {
			mStorageManager = (StorageManager) mActivity
					.getSystemService(Context.STORAGE_SERVICE);
			try {
				mMethodGetPaths = mStorageManager.getClass().getMethod(
						"getVolumePaths");
				// 通过调用类的实例mStorageManager的getClass()获取StorageManager类对应的Class对象
				// getMethod("getVolumePaths")返回StorageManager类对应的Class对象的getVolumePaths方法，这里不带参数
				// getDeclaredMethod()----可以不顾原方法的调用权限
				// mMethodGetPathsState=mStorageManager.getClass().
				// getMethod("getVolumeState",String.class);//String.class形参列表
			} catch (NoSuchMethodException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 判断SDcard是否准备好
	 *
	 * @return
	 */
	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获得内置或外置存储空间的路径
	 *
	 * @return
	 */
	public static String getSdCardPath() {
		if (isSdCardExist()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	/**
	 * 读取文件BufferedReader
	 *
	 * @param path
	 * @param fname
	 * @return
	 */
	public static StringBuffer readFileToSD(String path, String fname) {
		StringBuffer buffer = new StringBuffer();
		File f = new File(path, fname);
		if (f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(f));
				String readString = "";
				while ((readString = reader.readLine()) != null) {
					buffer.append(readString);
				}
				reader.close();
				return buffer;
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 写文件BufferedWriter
	 *
	 * @param path
	 * @param fname
	 * @param content
	 * @return
	 */
	@SuppressWarnings("resource")
	public static boolean writeFileToSD(String path, String fname,
			String content) {
		File file = new File(path);
		if (file.exists()) {
			File files = new File(path, fname);
			try {
				BufferedWriter writer = new BufferedWriter(
						new FileWriter(files));
				writer.write(content);
				writer.flush();
				return true;
			} catch (IOException e) {
				return false;
			}
		} else {
			file.mkdirs();
			File files = new File(path, fname);
			try {
				BufferedWriter writer = new BufferedWriter(
						new FileWriter(files));
				writer.write(content);
				writer.flush();
				return true;
			} catch (IOException e) {
				return false;
			}
		}
	}

	/**
	 *
	 * @Title: deleteFileToSD
	 * @Description: 删除指定目录的文件
	 * @param @param path
	 * @param @param fname
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean deleteFileToSD(String path, String fname) {
		try {
			File f = new File(path, fname);
			if (f.exists()) {
				return f.delete();
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * 获得data的路径
	 *
	 * @return
	 */
	public static String getRootFilePath() {
		if (isSdCardExist()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath:
																				// /data/data/
		}
	}

	/**
	 * 获得cache的路径
	 *
	 * @return
	 */
	public static String getCacheFilePath() {
		if (isSdCardExist()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/com.ud.server/caches/";
		} else {
			return Environment.getDataDirectory().getAbsolutePath()
					+ "/data/com.ud.server/caches/";
		}
	}

	/**
	 * 获得download的路径
	 *
	 * @return
	 */
	public static String getDowFilePath() {
		if (isSdCardExist()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/com.ud.server/download/";// filePath:/sdcard/
		}
		return null;
	}

	/**
	 * 获得SDCard/Android/data/你的应用的包名/files/目录
	 *
	 * @return
	 */
	public static String getFilesPathForSd() {
		if (isSdCardExist()) {
			return BaseApplication.getContext().getExternalCacheDir()
					.getAbsolutePath();
		}
		return null;
	}

	/**
	 * 获得SDCard/Android/data/你的应用包名/cache/目录
	 *
	 * @return
	 */
	public static String getCachePathForSd() {
		if (isSdCardExist()) {
			return BaseApplication.getContext().getExternalCacheDir()
					.getAbsolutePath();
		} else {
			return Environment.getDataDirectory().getAbsolutePath()
					+ "/data/com.ud.server/caches/";
		}
	}

	/**
	 * 获取手机内部剩余存储空间
	 *
	 * @return
	 */
	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内部总的存储空间
	 *
	 * @return
	 */
	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 获取SDCARD剩余存储空间
	 *
	 * @return
	 */
	public static long getAvailableExternalMemorySize() {
		if (isSdCardExist()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	/**
	 * 获取SDCARD总的存储空间
	 *
	 * @return
	 */
	public static long getTotalExternalMemorySize() {
		if (isSdCardExist()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	/**
	 * 获取系统总内存
	 *
	 * @param context
	 *            可传入应用程序上下文。
	 * @return 总内存大单位为B。
	 */
	public static long getTotalMemorySize(Context context) {
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine
					.indexOf("MemTotal:"));
			br.close();
			return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当前可用内存，返回数据以字节为单位。
	 *
	 * @param context
	 *            可传入应用程序上下文。
	 * @return 当前可用内存单位为B。
	 */
	public static long getAvailableMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}

	private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
	private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");

	/**
	 * 单位换算
	 *
	 * @param size
	 *            单位为B
	 * @param isInteger
	 *            是否返回取整的单位
	 * @return 转换后的单位
	 */
	public static String formatFileSize(long size, boolean isInteger) {
		DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
		String fileSizeString = "0M";
		if (size < 1024 && size > 0) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1024 * 1024) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1024 * 1024 * 1024) {
			fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
		} else {
			fileSizeString = df.format((double) size / (1024 * 1024 * 1024))
					+ "G";
		}
		return fileSizeString;
	}

	public static boolean checkNetState(Context context) {
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	public static boolean checkNetworkState(Context mContext) {
		boolean flag = false;
		// 得到网络连接信息
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 去进行判断网络是否连接
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}

	/**
	 * 检测Sdcard是否存在
	 *
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

}
