package com.ud.client.app_api.widget.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ud.client.app_api.R;


public class ProgressDlg extends ProgressDialog {
	private LinearLayout mLinerBg;
	private TextView hintText;
	private String text;
	public ProgressDlg(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_dialog);
		mLinerBg =(LinearLayout) findViewById(R.id.dialog_progress);
		mLinerBg.getBackground().setAlpha(150);
		hintText = (TextView)findViewById(R.id.text);
		if(null!=text)hintText.setText(text);
	}

	public void setText(String text){
		this.text = text;
	}

}
