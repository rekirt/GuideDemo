package com.example.lc.guidedemo;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Author: lc
 * Email: rekirt@163.com
 * Date: 16-7-10
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    protected void openActivity(Class clz){
        Intent intent = new Intent();
        intent.setClass(this,clz);
        startActivity(intent);
    }
}
