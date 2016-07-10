package com.ud.client.app_api.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ud.client.app_api.bussiness.IBussiness;
import com.ud.client.app_api.utils.Logger;
import com.ud.client.app_api.utils.ProgressDialogUtil;
import com.ud.client.app_api.utils.SharedPreferencesUtil;
import com.ud.client.app_api.utils.StringUtil;
import com.ud.client.app_api.utils.ToastManager;
import com.ud.client.app_api.utils.UIUtil;
import com.ud.client.app_api.utils.ViewUtil;
import com.ud.client.app_api.widget.LoadingPage;
import com.ud.client.app_api.widget.progress.ProgressDlg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Author: lc
 * Email: rekirt@163.com
 * Date: 16-3-24.
 */
public abstract class BaseFragment<T> extends Fragment implements IBussiness<T> {

    public LoadingPage mContentView;
    protected ProgressDlg mProgressDialog = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null==mContentView){
            mContentView = new LoadingPage(UIUtil.getContext()) {
                @Override
                public View createSuccessView() {
                    return BaseFragment.this.createLoadedView();
                }

                @Override
                protected LoadResult load() {
                    return BaseFragment.this.load();
                }
            };
        }else{
            ViewUtil.removeSelfFromParent(mContentView);
        }
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setListener();
        initData();
    }

    protected abstract LoadingPage.LoadResult load();

    protected abstract View createLoadedView();

    protected void initView(){}
    protected void setListener(){}
    protected void initData(){}

    public void show(){
        if(null!=mContentView){
            mContentView.show();
        }
    };


    protected LoadingPage.LoadResult check(Object mData) {
        if(null==mData)return LoadingPage.LoadResult.ERROR;
        if(mData instanceof List){
            List list = (List)mData;
            if(list.size()<0)return LoadingPage.LoadResult.EMPTY;
        }
        return LoadingPage.LoadResult.SUCCESS;
    }

    @Override
    public void onBusinessSucc(int bCode, T obj) {
    }

    @Override
    public void onBusinessFail(int bCode, Object obj) {
        showMessage(obj.toString());
    }

    @Override
    public void setNetWork() {
    }

    @Override
    public void requestHandler(Object obj) {
    }

    protected void openActivity(Class clz){
        Intent intent = new Intent(getActivity(),clz);
        startActivity(intent);
    }

    protected boolean isLogin(){
        boolean flag = false;
        String json = SharedPreferencesUtil.getInstance().getLoginResponse();
        if(!StringUtil.isEmpty(json)){
            try {
                JSONObject job = new JSONObject(json);
                int customid = job.getInt("customerid");
                String regdate = job.getString("regdate");
                Logger.e("regdate====" + regdate);
                if(!StringUtil.isEmpty(regdate)){
                    flag = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Logger.e("login===" + flag);
        return flag;
    }

    public void showMessage(String message){
        ToastManager.getInstance().showToast(getActivity(), message);
    }

    protected void showMyDialog(String message){
        mProgressDialog = ProgressDialogUtil.showDialog(getActivity(), true, message);
        mProgressDialog.show();
    }
    protected void hidenMyDialog(){
        if(null!=mProgressDialog)mProgressDialog.dismiss();
    }

    /**
     * 动态申请权限
     * @param reqCode
     * @param permission
     */
    protected boolean checkPermission(int reqCode, String permission) {
        boolean flag;
        if (Build.VERSION.SDK_INT >= 23) {
            //是否拥有权限
            int checkPermissionResult = UIUtil.getContext().checkSelfPermission(permission);
            if (checkPermissionResult != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                requestPermissions(new String[]{permission}, reqCode);
                flag = false;
            } else {
                //获取到权限
                flag = true;
            }
        }else{
            flag = true;
        }
        return flag;
    }

}
