package com.ud.client.app_api.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				Logger.e(e);
			}
		}
		return true;
	}
}
