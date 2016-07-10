package com.ud.client.app_api.bussiness;


/**
 * Author: lc
 * Email: rekirt@163.com
 * Date: 16-5-3.
 */
public interface IBussiness<T> {
    public void onBusinessSucc(int bCode, T obj);

    public void onBusinessFail(int bCode, Object obj);

    public void setNetWork();

    public void requestHandler(Object obj);
}
