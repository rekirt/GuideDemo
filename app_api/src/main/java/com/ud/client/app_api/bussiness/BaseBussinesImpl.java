package com.ud.client.app_api.bussiness;

import com.alibaba.fastjson.JSON;
import com.ud.client.app_api.network.http.HttpRequestImpl;
import com.ud.client.app_api.network.http.IHttpRequest;

/**
 * Author: lc
 * Email: rekirt@163.com
 * Date: 16-5-3.
 */
public abstract class BaseBussinesImpl implements IHttpRequest {

    /**
     * 请求码
     */
    private int requestCode;

    /**
     * 请求的key
     */
    private String requestKey;

    /**
     *
     * 请求的json对象
     */
    private Object data;

    protected HttpRequestImpl mHttpRequest = null;
    protected IBussiness iBaseBusiness = null;

    public BaseBussinesImpl() {
        this.mHttpRequest = new HttpRequestImpl(this);
    }

    public BaseBussinesImpl(IBussiness baseBusiness) {
        this.iBaseBusiness = baseBusiness;
        this.mHttpRequest = new HttpRequestImpl(this);
    }

    public BaseBussinesImpl(IBussiness baseBusiness, int requestCode) {
        this.iBaseBusiness = baseBusiness;
        this.mHttpRequest = new HttpRequestImpl(this);
        this.requestCode =requestCode;
        this.mHttpRequest.setRequestCode(getRequestCode());
    }

    @Override
    public void accessSucc(int requestCode, Object obj) {
        if(null!=iBaseBusiness){
            iBaseBusiness.onBusinessSucc(requestCode,obj);
        }
    }

    @Override
    public void accessFail(int requestCode, Object obj) {
        if(null!=iBaseBusiness){
            iBaseBusiness.onBusinessFail(requestCode,obj);
        }
    }

    @Override
    public abstract void startRequest();

    @Override
    public abstract Object startRequestSync();

    @Override
    public void noNetWork() {
        if (null != iBaseBusiness) {
            iBaseBusiness.setNetWork();
        }
    }

    @Override
    public void requestHandler(Object obj) {
        if(null!=iBaseBusiness)iBaseBusiness.requestHandler(obj);
    }

    public void recycle() {
        mHttpRequest = null;
        iBaseBusiness = null;
    }

    /**
     * bean转换为json
     * @return
     */
    public String dataToJson(){
        if(null!=getData()){
            return JSON.toJSONString(getData());
        }
        return null;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        mHttpRequest.setRequestCode(requestCode);
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
        mHttpRequest.setRequestKey(requestKey);
    }
}
