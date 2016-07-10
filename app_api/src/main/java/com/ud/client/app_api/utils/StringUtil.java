package com.ud.client.app_api.utils;

import android.util.Base64;

/**
 *
 * @Author lc
 */
public class StringUtil {
	/**
	 * 
	* @Method: reverse 
	* @Description:反转字符串  
	* @param input
	* @return
	 */
	public static String reverse(String input){
		StringBuffer sb = new StringBuffer();
		for(int i=input.length()-1;i>=0;i--){
			sb.append(input.charAt(i));
		}
		return sb.toString();
	}
	
	
    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     * 
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static String bytes2Base64(byte[] buff){
        return Base64.encodeToString(buff, Base64.DEFAULT);
    }
	
}
