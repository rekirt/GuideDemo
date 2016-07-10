package com.ud.client.app_api.network.http;


/**
 * @ClassName IHttpRequest
 * @Description  网络访问回调接口，继承此接口后可以获得访问成功和失败的数据
 * @author admin
 */
public interface IHttpRequest {
    /**
     * 请求成功的回调
     * @param requestCode
     * @param obj
     */
    public void accessSucc(int requestCode, Object obj);

    /**
     * 请求失败的回调
     * @param requestCode
     * @param obj
     */
    public void accessFail(int requestCode, Object obj);

    /**
     * 开启一个异步请求
     */
    public void startRequest();

    /**
     * 开启一个同步请求
     */
    public Object startRequestSync();

    /**
     * 没有网络
     */
    public void noNetWork();

    /**
     * 该请求的handler
     * @param obj
     */
    public void requestHandler(Object obj);

}
