package owt.sample.conference.hexmeet.utils;

import android.util.Log;


import owt.sample.conference.hexmeet.Constant;


/**
 * log 工具类，统一去调用
 * Created by hanqq on 2020/06/30
 */
public class LogUtils {

    private static final String TAG = "HEX_LOG:";

    public static void v(String st) {
        if (Constant.LOG_SWITCH) {
            Log.v(TAG, init(st));
        }
    }

    public static void v(int num) {
        if (Constant.LOG_SWITCH) {
            Log.v(TAG, init(String.valueOf(num)));
        }
    }

    public static void v(String tag, String st) {
        if (Constant.LOG_SWITCH) {
            Log.v(TAG + tag, init(st));
        }
    }

    public static void v(String tag, int num) {
        if (Constant.LOG_SWITCH) {
            Log.v(TAG + tag, init(String.valueOf(num)));
        }
    }

    public static void d(String st) {
        if (Constant.LOG_SWITCH) {
            Log.d(TAG, init(st));
        }
    }

    public static void d(int num) {
        if (Constant.LOG_SWITCH) {
            Log.d(TAG, init(String.valueOf(num)));
        }
    }

    public static void d(String tag, String st) {
        if (Constant.LOG_SWITCH) {
            Log.d(TAG + tag, init(st));
        }
    }

    public static void d(String tag, int num) {
        if (Constant.LOG_SWITCH) {
            Log.d(TAG + tag, init(String.valueOf(num)));
        }
    }

    public static void e(String st) {
        if (Constant.LOG_SWITCH) {
            Log.e(TAG, init(st));
        }
    }

    public static void e(int num) {
        if (Constant.LOG_SWITCH) {
            Log.e(TAG, init(String.valueOf(num)));
        }
    }

    public static void e(String tag, String st) {
        if (Constant.LOG_SWITCH) {
            Log.e(TAG + tag, init(st));
        }
    }

    public static void e(String tag, int num) {
        if (Constant.LOG_SWITCH) {
            Log.e(TAG + tag, init(String.valueOf(num)));
        }
    }

    public static void w(String st) {
        if (Constant.LOG_SWITCH) {
            Log.w(TAG, init(st));
        }
    }

    public static void w(int num) {
        if (Constant.LOG_SWITCH) {
            Log.w(TAG, init(String.valueOf(num)));
        }
    }

    public static void w(String tag, String st) {
        if (Constant.LOG_SWITCH) {
            Log.w(TAG + tag, init(st));
        }
    }

    public static void w(String tag, int num) {
        if (Constant.LOG_SWITCH) {
            Log.w(TAG + tag, init(String.valueOf(num)));
        }
    }

    /**
     * 增加额外的显示
     */
    public static String init(String string) {
        StringBuffer st = new StringBuffer();
        st.append(string);
        return st.toString();
    }
}
