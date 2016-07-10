package com.ud.client.app_api.utils;

import java.util.List;

/**
 * Corporation www.ihardstone.com
 * <p/>
 * Created by lc on 15-10-26.
 * <p/>
 * email: rekirt@163.com
 */
public class ListUtil {

    public static <V> int getSize(List<V> sourceList) {
        return sourceList == null ? 0 : sourceList.size();
    }
}
