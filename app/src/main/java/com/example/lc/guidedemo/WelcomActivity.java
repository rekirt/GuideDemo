package com.example.lc.guidedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: lc
 * Email: rekirt@163.com
 * Date: 16-7-10
 */
public class WelcomActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
              openActivity(MainActivity.class);
                finish();
            }
        },3000);
    }
}
