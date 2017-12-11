package com.march.lightadapter.helper;

import android.util.Log;

/**
 * CreateAt : 2017/1/7
 * Describe : 日志打印管理
 *
 * @author chendong
 */

public class LightLogger {

    public static final boolean isDebug = true;

    public static void e(String tag, Object msg) {
        if (isDebug)
            Log.e("LightAdapter|" + tag, msg == null ? "msg is null" : msg.toString());
    }
}
