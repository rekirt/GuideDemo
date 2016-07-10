package com.ud.client.app_api.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 功能说明：应用程序管理类
 * @author lc
 *
 */
public class AppManager {
    private static List<Activity> activityStack;// activity的栈
    private static AppManager instance;// AppManager对象实现

    private AppManager() {
    }

    /**
     * 单一实例(恶汉方式)
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        // 创建实例
        if (activityStack == null) {
            activityStack = new LinkedList<Activity>();
        }
        // 添加到堆栈
        activityStack.add(activity);
        Logger.e("堆栈中添加一个actvity后的个数===" + activityStack.size());
    }

    public void removeActivity(Activity activity) {
        activity.finish();
        activityStack.remove(activity);// 先从堆栈里移除
        activity = null;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();// 再结束当前的Activity
            activity = null;// 之后把引用变为null，结束内存
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivitys(Class<?> ...clss) {
        for (Activity activity : activityStack) {// 遍历堆栈,
            for(Class cls:clss) {
                if (activity.getClass().equals(cls)) {// 匹配当前的类对象和结束类名是否一样
                    finishActivity(activity);
                    break;
                }
            }
        }
        Logger.e("堆栈中移除"+clss.length+"个actvity后的个数===" + activityStack.size());
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {// 遍历堆栈
            if (activity.getClass().equals(cls)) {// 匹配当前的类对象和结束类名是否一样
                finishActivity(activity);
            }
        }
        Logger.e("堆栈中移除一个actvity后的个数===" + activityStack.size());
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (null == activityStack)
            return;
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {// 遍历堆栈中activity，来关闭
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();// 清空堆栈
    }

    /**
     * 根据上下文，退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();// 关闭所有的activity
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0); // 关闭虚拟机
        } catch (Exception e) {
        }
    }
}