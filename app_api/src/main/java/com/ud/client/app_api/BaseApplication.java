package com.ud.client.app_api;


import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.StrictMode;

import com.ud.client.app_api.constants.Constant;
import com.ud.client.app_api.utils.Logger;
import com.ud.client.app_api.utils.SharedPreferencesUtil;


public class BaseApplication extends Application {
	private static BaseApplication app = null;
	private static Context context = null;
	private static SharedPreferencesUtil sharedPreferencesUtil = null;
	private static Handler mMainHandler;
	private static Looper mMainLooper;
	private static Thread mMainThead;
	private static int mMainThreadId;

//	private static final HandlerThread handlerThread = new HandlerThread("BaseApplication");
//	private static final Handler mHandler = new Handler(handlerThread.getLooper());

	static {
//		handlerThread.start();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		super.onCreate();
		if(Constant.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){//如果版本大于2.3并且处于开发者模式开启
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
		}
		app = this;
		context = getApplicationContext();
		sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
		mMainHandler = new Handler();
		mMainLooper = getMainLooper();
		mMainThead = Thread.currentThread();
		mMainThreadId = android.os.Process.myTid();
	}

	public static BaseApplication getApplication(){
		return app;
	}

	public static Context getContext(){
		return context;
	}

	/**
	 * 主线线程的handler
	 * @return
	 */
	public static Handler getMainThreadHandler(){
		return mMainHandler;
	}

	/**
	 * 主线程的Looper
	 * @return
	 */
	public static Looper getmMainLooper(){
		return mMainLooper;
	}

	public static Thread getMainThread(){
		return mMainThead;
	}

	public static  int getMainThreadId(){
		return mMainThreadId;
	}

	public static SharedPreferencesUtil getSharedPreferences(){
		return sharedPreferencesUtil;
	}


	@Override
	public void onLowMemory() {
        /**
         * 低内存的时候主动释放所有线程和资源 PS:这里不一定每被都调用
         */
    	Logger.d("SmartMapApplication  onLowMemory");
		super.onLowMemory();
	}
	
	@Override
	public void onTerminate() {
        /**
         * 系统退出的时候主动释放所有线程和资源 PS:这里不一定被都调用
         */
        Logger.d("SmartMapApplication  onTerminate");
		super.onTerminate();
	}
	
	
	
}
