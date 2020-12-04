package owt.sample.conference.hexmeet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;


import com.google.gson.Gson;

import owt.sample.conference.hexmeet.BaseApplication;
import owt.sample.conference.hexmeet.Constant;


/**
 * 文件存储
 * Created by hanqq on 2020/7/1
 */
public class SharePreferences {
    private static volatile SharePreferences sharePreferences;
    public SharedPreferences prefs;
    // camera
    private final String PREFERENCE_CAMERA = "preference_camera";
    // server_url
    private final String PREFERENCE_SERVER_URL = "preference_server_url";
    // roomid
    private final String PREFERENCE_ROOMID = "preference_roomid";
    // 分辨率
    private final String PREFERENCE_RESOLUTION = "preference_resolution";
    // 帧率
    private final String PREFERENCE_FRAMERATE = "preference_framerate";
    // video codec
    private final String PREFERENCE_VIDEO_CODEC = "preference_video_codec";

    // 是否第一次启动
    private final String PREFERENCE_CONFIGURES_FIRST_INIT = "preference_configures_first_init";


    public static SharePreferences getInstance() {
        if (sharePreferences == null) {
            synchronized (SharePreferences.class) {
                if (sharePreferences == null) {
                    sharePreferences = new SharePreferences(BaseApplication.getApplication());
                }
            }
        }
        return sharePreferences;
    }

    private SharePreferences(Context context) {
        prefs = context.getSharedPreferences(Constant.SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    // camera true 前置 false 后置
    public void setCameraFront(boolean isFront) {
        putBoolean(PREFERENCE_CAMERA, isFront);
    }

    public Boolean getCameraFront() {
        return getBoolean(PREFERENCE_CAMERA);
    }

    // 设置 ServerUrl
    public void setServerUrl(String serverUrl) {
        putString(PREFERENCE_SERVER_URL, serverUrl);
    }

    public String getServerUrl() {
        return getString(PREFERENCE_SERVER_URL);
    }

    // 设置 roomID
    public void setRoomID(String roomid) {
        putString(PREFERENCE_ROOMID, roomid);
    }

    public String getRoomID() {
        return getString(PREFERENCE_ROOMID);
    }

    // 设置 Resolution
    public void setResolution(Point resolution) {
        putString(PREFERENCE_RESOLUTION, GsonUtils.toJson(resolution));
    }

    public Point getResolution() {
        return GsonUtils.toObject(getString(PREFERENCE_RESOLUTION), Point.class);
    }

    // 设置 Framerate
    public void setFramerate(int framerate) {
        putInt(PREFERENCE_FRAMERATE, framerate);
    }

    public int getFramerate() {
        return getInt(PREFERENCE_FRAMERATE);
    }

    // 设置 codec
    public void setCodec(String codec) {
        putString(PREFERENCE_VIDEO_CODEC, codec);
    }

    public String getCodec() {
        return getString(PREFERENCE_VIDEO_CODEC);
    }


    /**
     * 存放普通数据的方法
     *
     * @param key  存储数据的键
     * @param data 存储数据的值
     */
    private void putString(String key, String data) {
        prefs.edit().putString(key, data).apply();
    }

    /**
     * 读取普通数据的方法
     *
     * @param key 要读取数据的key
     * @return 要读取的数据
     */
    private String getString(String key) {
        return prefs.getString(key, "");
    }

    /**
     * 存放普通数据的方法
     *
     * @param key  存储数据的键
     * @param data 存储数据的值
     */
    protected void putInt(String key, int data) {
        prefs.edit().putInt(key, data).apply();
    }

    /**
     * 读取普通数据的方法
     *
     * @param key 要读取数据的key
     * @return 要读取的数据
     */
    protected int getInt(String key) {
        return prefs.getInt(key, 0);
    }

    /**
     * 移除相关key对应的item
     *
     * @param key 需要移除的key
     */
    protected void remove(String key) {
        prefs.edit().remove(key).apply();
    }


    protected void putLong(String key, long data) {
        prefs.edit().putLong(key, data).apply();
    }

    protected Long getLong(String key) {
        return prefs.getLong(key, 0);
    }

    protected void putBoolean(String key, Boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    protected Boolean getBoolean(String value) {
        return prefs.getBoolean(value, false);
    }

    /**
     * 清除相关prefs数据
     */
    public void clear() {
        prefs.edit().clear().apply();
    }

}
