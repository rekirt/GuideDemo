package com.ud.client.app_api.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * 
 * @Author lc
 */
public class ToastManager {
	
	private static ToastManager mInstance = null;
	private ToastInfo mPreShowToastInfo;
	
	/**两个相同的toast显示的间隔*/
	private final long mShowSameToastInterval = 2000;
	private Toast mToast;
	
	private ToastManager(){};
	
	public static ToastManager getInstance(){
		if(null==mInstance){
			synchronized (ToastManager.class) {
				if(null==mInstance){
					mInstance = new ToastManager();
				}
			}
		}
		return mInstance;
	};
	
	public void showToast(Context context,String message){
		if(!TextUtils.isEmpty(message)){
			showToast(context, message, Toast.LENGTH_SHORT);
		}
	}
	
	public void showToast(Context context,int resId){
		if(null!=context){
			String message = context.getString(resId);
			showToast(context, message, Toast.LENGTH_SHORT);
		}
	}
	
	public void showToastLong(Context context,String message){
		if(!TextUtils.isEmpty(message)){
			showToast(context, message, Toast.LENGTH_LONG);
		}
	}
	
	public void showToastLong(Context context,int resId){
		if(null!=context){
			String message = context.getString(resId);
			showToast(context, message, Toast.LENGTH_LONG);
		}
	}
	/**
	 * 
	* @Method: showToast 
	* @Description: 
	* @param context
	* @param message
	* @param duration
	 */
	public void showToast(Context context,String message,int duration){
		if(null==mPreShowToastInfo){//当前只有一个toast显示
			updateToastInfoAndShowToast(context,message,duration);
		}else{//之前有toast被显示过
			if(!TextUtils.isEmpty(mPreShowToastInfo.text) && mPreShowToastInfo.text.equals(message)){//前后显示的两个toast一模一样
				/*
				 * 当接下来要显示的Toast的文本与先前的相同时,按照以下规则进行显示. #1> 如果两个Toast之间的时间间隔大于预定义的时间间隔,则显示第二个Toast. #2>
				 * 如果两个Toast的Context不相同时,则显示第二个Toast. #3> 如果不满足1,2则只更新时间.（PS：已改为不更新时间，效果为toast消失时再点击能正常提示）
				 */
				Context preContext = mPreShowToastInfo.contextRef.get();//前一个toast显示使用的context
				if(System.currentTimeMillis()-mPreShowToastInfo.showTime>=mShowSameToastInterval || (preContext!=context)){
					updateToastInfoAndShowToast(context, null, duration);
				}
			}else{//前后显示的toast文字不一样
				updateToastInfoAndShowToast(context, message, duration);
			}
		}
	}
	
	/**
	 * 
	* @Method: updateToastInfoAndShowToast 
	* @Description: 先保存toast信息，再显示 
	* @param context
	* @param text
	* @param duration
	 */
    private void updateToastInfoAndShowToast(Context context, String text,int duration) {
    	if(null==mPreShowToastInfo){
			mPreShowToastInfo = new ToastInfo();
		}
		if(null!=text){
			mPreShowToastInfo.text = text;
		}
		if(null!=context){
			mPreShowToastInfo.contextRef = new WeakReference<Context>(context);
		}
		if(null==mToast){	
			mToast = Toast.makeText(context, mPreShowToastInfo.text, duration);
		}else{
			mToast.setText(mPreShowToastInfo.text);
			mToast.setDuration(duration);
		}
		mToast.setGravity(Gravity.CENTER,0,0);
		mToast.show();
		mPreShowToastInfo.showTime = System.currentTimeMillis();
	}

	private static class ToastInfo {
        WeakReference<Context> contextRef;
        String text;
        long showTime;
    }
}
