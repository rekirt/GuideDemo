package com.ud.client.app_api.utils;

import android.content.Context;

import com.ud.client.app_api.R;
import com.ud.client.app_api.widget.progress.ProgressDlg;


public class ProgressDialogUtil {
	private static ProgressDlg progress;

	public static ProgressDlg showDialog(Context context, boolean iscancle,String message) {
		if (progress == null) {
			progress = new ProgressDlg(context, R.style.MenuDialogStyle_Progress);
		} else {
			if (progress.getContext() != context) {
				progress = null;
				progress = new ProgressDlg(context, R.style.MenuDialogStyle_Progress);
			}
		}
		if(null!=message)progress.setText(message);
		progress.setCanceledOnTouchOutside(false);
		progress.setCancelable(iscancle);
		return progress;
	}

	public static void dismiss() {
		if (progress != null) {
			try {
				progress.dismiss();
			} catch (Exception e) {
			}
		}
	}
}
