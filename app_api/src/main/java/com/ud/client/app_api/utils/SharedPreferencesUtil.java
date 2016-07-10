package com.ud.client.app_api.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ud.client.app_api.BaseApplication;
import com.ud.client.app_api.constants.Constant;

/**
 *
 * @Corporation isoftstone
 * @Author lc
 */
public class SharedPreferencesUtil {
	private static SharedPreferences mUserShareParences;
	private static SharedPreferences mCacheShareParences;
	private static Editor mUserEditor;
	private static Editor mCacheEditor;
	
	private static SharedPreferencesUtil instance = null;

	private SharedPreferencesUtil(){
		if (null == mUserShareParences) {
			mUserShareParences = BaseApplication.getContext().getSharedPreferences(Constant.UDSERVER_USER, Context.MODE_PRIVATE);
			mUserEditor = mUserShareParences.edit();
		}
		if(null==mCacheShareParences){
			mCacheShareParences = BaseApplication.getContext().getSharedPreferences(Constant.UDSERVER_CACHE, Context.MODE_PRIVATE);
			mCacheEditor = mCacheShareParences.edit();
		}
	}
	
	public static SharedPreferencesUtil getInstance() {
		if(null==instance){
			synchronized (SharedPreferencesUtil.class) {
				if(null==instance)
					instance = new SharedPreferencesUtil();
			}
		}
		return instance;
	}

	public boolean isFirstTime() {
		return mCacheShareParences.getBoolean("isFirstTime", true);
	}

	public void saveFirstTime(boolean flag){
		mCacheEditor.putBoolean("isFirstTime", flag).apply();
	}

	public void saveBackupUUID(String uuid) {
		mCacheEditor.putString("appUUID", uuid).apply();
	}

	public String getBackupUUID() {
		return mCacheShareParences.getString("appUUID", "");
	}

	public void saveLoginResponse(String longinresoponse) {
		mUserEditor.putString("longinresoponse", longinresoponse).apply();
	}

	public String getLoginResponse() {
		return mUserShareParences.getString("longinresoponse", "");
	}

	public void loginOut() {
		mUserEditor.clear().apply();
	}

	public void saveWorkList(String worklist) {
		mCacheEditor.putString("worklist", worklist).apply();
	}

	public String getWorkList(){
		return mCacheShareParences.getString("worklist", "");
	}

	public void savePushSwitch(boolean b) {
		mUserEditor.putBoolean("pushswitch", b).apply();
	}

	public boolean getPushSwitch(){
		return mUserShareParences.getBoolean("pushswitch",false);
	}
}
