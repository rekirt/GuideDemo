package com.ud.client.app_api.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ud.client.app_api.R;
import com.ud.client.app_api.bussiness.IBussiness;
import com.ud.client.app_api.utils.Logger;
import com.ud.client.app_api.utils.ProgressDialogUtil;
import com.ud.client.app_api.utils.SharedPreferencesUtil;
import com.ud.client.app_api.utils.StringUtil;
import com.ud.client.app_api.utils.ToastManager;
import com.ud.client.app_api.utils.UIUtil;
import com.ud.client.app_api.widget.progress.ProgressDlg;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseActivity<T> extends FragmentActivity implements IBussiness<T> {

    private FrameLayout fl_title_layout;
    private ImageView leftButton;
    private TextView titleButton;
    private ImageView rightButton;
    private TextView tv_right_text_button;
    private boolean unTitle = false;
    protected ProgressDlg mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initIntentValue();
        if(unTitle){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            if(0!=getContentResId()){
                setContentView(getContentResId());
            }else{
                setContentView(getContentView());
            }
        }else{
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
            if(0!=getContentResId()){
                setContentView(getContentResId());
            }else{
                setContentView(getContentView());
            }
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
            fl_title_layout = (FrameLayout)findViewById(R.id.fl_title_layout);
            leftButton = (ImageView)findViewById(R.id.iv_back);
            titleButton = (TextView)findViewById(R.id.tv_title);
            rightButton = (ImageView)findViewById(R.id.iv_option);
            tv_right_text_button = (TextView)findViewById(R.id.tv_right_text_button);

            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        initView();
        setListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected int getContentResId() {
        return 0;
    }

    protected View getContentView(){
        TextView view = new TextView(UIUtil.getContext());
        view.setText("视图为空");
        view.setGravity(Gravity.CENTER);
        return view;
    };

    public FrameLayout getTitleLayout(){
        return fl_title_layout;
    }

    public ImageView getLeftButton() {
        return leftButton;
    }

    public TextView getTitleButton() {
        return titleButton;
    }

    public ImageView getRightButton() {
        return rightButton;
    }
    public TextView getRightTextButton(){
        return tv_right_text_button;
    }

    public void setUnTitle(boolean unTitle) {
        this.unTitle = unTitle;
    }

    protected void initIntentValue(){};
    protected void initView(){};
    protected void setListener() {}
    protected void initData() {}
    protected void openActivity(Class cls){
        Intent intent = new Intent(this,cls);
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
                Logger.e("regdate===="+regdate);
                if(!StringUtil.isEmpty(regdate)){
                    flag = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return flag;
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

    protected void showMessage(String message){
        ToastManager.getInstance().showToast(this,message);
    }

    protected void showMessage(int resId){
        ToastManager.getInstance().showToast(this,resId);
    }

    protected void showMyDialog(String message){
        mProgressDialog = ProgressDialogUtil.showDialog(this, true,message);
        mProgressDialog.show();
    }

    protected void hidenMyDialog(){
        if(null!=mProgressDialog)mProgressDialog.dismiss();
    }

}
