package owt.sample.conference.hexmeet;

import android.app.Application;
import android.content.Context;
import android.os.Process;


/**
 * Application 类 初始化数据
 * Created by yin13 on 2020/6/30
 */
public class BaseApplication extends Application {
    // 获取到主线程的id
    private static int mMainThreadId;
    private static Context mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mMainThreadId = Process.myTid();
        mApplication = getApplicationContext();
    }

    public static Context getApplication() {
        return mApplication;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

}
