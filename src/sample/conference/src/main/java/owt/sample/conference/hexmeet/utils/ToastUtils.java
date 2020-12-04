package owt.sample.conference.hexmeet.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import owt.sample.conference.hexmeet.BaseApplication;


/**
 * 吐司相关工具类
 * Created by hanqq on 2020/06/30
 */
public class ToastUtils {

    public static void show(int stringID) {
        show(BaseApplication.getApplication().getString(stringID), Toast.LENGTH_SHORT);
    }

    public static void show(String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void showLong(String text) {
        show(text, Toast.LENGTH_LONG);
    }

    private static void show(final String text, final int duration) {
        if (TextUtils.isEmpty(text)) return;
        if (android.os.Process.myTid() == BaseApplication.getMainThreadId()) {
            showToast(text, duration);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showToast(text, duration);
                }
            });
        }
    }

    private static void showToast(String text, int duration) {
        Toast.makeText(BaseApplication.getApplication(), text, duration).show();
    }

}