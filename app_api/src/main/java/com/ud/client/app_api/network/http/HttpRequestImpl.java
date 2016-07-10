package com.ud.client.app_api.network.http;

import com.ud.client.app_api.BaseApplication;
import com.ud.client.app_api.constants.Constant;
import com.ud.client.app_api.utils.CommonUtil;
import com.ud.client.app_api.utils.SecurityUtil;

/**
 * Author: lc
 * Email: rekirt@163.com
 * Date: 15-6-24.
 */
public class HttpRequestImpl {
	private IHttpRequest iHttpRequest = null;
	protected int requestCode = -1;
	private String requestKey = null;
	protected String httpUrl = null;

	public HttpRequestImpl(IHttpRequest httpRequest) {
		this.iHttpRequest = httpRequest;
	}

	/**
	 * 发起一个异步请求
	 * @param httpType
	 * @param url
	 * @param requestJson
	 * @param resp
	 */
	public void startRequest(String httpType, String url, String requestJson,final Class resp) {
		if (!CommonUtil.checkNetworkState(BaseApplication.getContext())) {
			this.iHttpRequest.noNetWork();
			return;
		}
		if(Constant.Config.DEVELOPER_MODE){
			httpUrl = url.replace(Constant.BASEURL,Constant.SERVER_TEST) + getRequestKey();
		}else{
			httpUrl = url.replace(Constant.BASEURL,Constant.SERVER_BUILD) + getRequestKey();
		}
		String data = SecurityUtil.aes(requestJson);
//		RequestParams rp = new RequestParams(genSignUrl(data));
//		rp.setHeader("Content-Type", "application/json");
//		rp.setCharset("utf-8");
//		rp.setConnectTimeout(Constant.CONNECT_TIMEOUT);
//		rp.setBodyContent(data);
//		Logger.e("请求的url===" + genSignUrl(data));
//		Logger.e("明文请求的json===" + requestJson);
//		Logger.e("加密请求的json===" + data);
//		x.http().post(rp, new Callback.CommonCallback<String>() {
//			@Override
//			public void onSuccess(String result) {
//				Logger.e("success===" + result);
//				if(!StringUtil.isEmpty(result)){
//					iHttpRequest.accessSucc(getRequestCode(), JSON.parseObject(result, resp));
//				}else{
//					onError(new RuntimeException(Constant.RESPONSE_EMPTY),true);
//				}
//			}
//
//			@Override
//			public void onError(Throwable ex, boolean isOnCallback) {
//				Logger.e("error===" + ex.toString());
//				iHttpRequest.accessFail(getRequestCode(), ex.toString());
//			}
//
//			@Override
//			public void onCancelled(CancelledException cex) {
//
//			}
//
//			@Override
//			public void onFinished() {
//
//			}
//		});
	}

	/**
	 * 发起一个同步请求，需要在子线程中发起
	 * @param httpType
	 * @param url
	 * @param requestJson
	 * @param resp
	 */
	public Object startRequestSync(String httpType, String url, String requestJson,final Class resp) {
		if (!CommonUtil.checkNetworkState(BaseApplication.getContext())) {
			this.iHttpRequest.noNetWork();
			return null;
		}
		if(Constant.Config.DEVELOPER_MODE){
			httpUrl = url.replace(Constant.BASEURL,Constant.SERVER_TEST) + getRequestKey();
		}else{
			httpUrl = url.replace(Constant.BASEURL,Constant.SERVER_BUILD) + getRequestKey();
		}
		String data = SecurityUtil.aes(requestJson);
//		RequestParams rp = new RequestParams(genSignUrl(data));
//		rp.setHeader("Content-Type", "application/json");
//		rp.setCharset("utf-8");
//		rp.setConnectTimeout(Constant.CONNECT_TIMEOUT);
//		rp.setBodyContent(data);
//		Logger.e("请求的url===" + genSignUrl(data));
//		Logger.e("明文请求的json===" + requestJson);
//		Logger.e("加密请求的json===" + data);
//		try {
//			String json = x.http().postSync(rp,String.class);
//			Logger.e("请求结果==="+json);
//			return json;
//		} catch (Throwable throwable) {
//			throwable.printStackTrace();
//		}
		return null;
	}
	/**
	 * 生成url
	 * @param data
	 * @return
	 */
	private String genSignUrl(String data) {
		String uuid = SecurityUtil.getUUID();
		String sign = SecurityUtil.md5(data + uuid + Constant.PASSWD);
		String url = httpUrl+"?v=1&key="+uuid+"&sign="+sign;
		return url;

	}

	public int getRequestCode() {
		return requestCode;
	}
	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public String getRequestKey() {
		return requestKey;
	}

	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}

}
