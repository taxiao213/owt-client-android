package owt.sample.conference.hexmeet.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import owt.base.Const;
import owt.sample.conference.MainActivity;
import owt.sample.conference.R;
import owt.sample.conference.hexmeet.Constant;
import owt.sample.conference.hexmeet.adapter.ResolutionAdapter;

/**
 * Created by hanqq on 2020/12/3
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class UIUtils {
    private static volatile UIUtils uiUtils;
    private int position = 0;

    private UIUtils() {
    }

    public static UIUtils getInstance() {
        if (uiUtils == null) {
            synchronized (UIUtils.class) {
                if (uiUtils == null) {
                    uiUtils = new UIUtils();
                }
            }
        }
        return uiUtils;
    }

    /**
     * 分辨率
     *
     * @param context
     * @param function
     */
    public void selectResolution(Context context, Function<Point> function) {
        if (context == null) return;
        position = 0;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(context);
        singleChoiceDialog.setTitle(context.getString(R.string.resolution_list));
        ArrayList<Point> resolutionList = Constant.getResolutionList();
        Point resolution = getResolution();
        String[] items = new String[resolutionList.size()];
        for (int i = 0; i < resolutionList.size(); i++) {
            Point point = resolutionList.get(i);
            items[i] = String.valueOf(point.x + " x " + point.y);
            if (point.x == resolution.x && point.y == resolution.y) {
                position = i;
            }
        }
        singleChoiceDialog.setSingleChoiceItems(items, position, (dialog, which) -> {
            position = which;
        });

        singleChoiceDialog.setPositiveButton("ok", (dialog, which) -> {
            if (function != null) {
                Point point = resolutionList.get(position);
                setResolution(point);
                function.action(point);
            }
        });
        singleChoiceDialog.show();
    }

    /**
     * 订阅分辨率
     *
     * @param context
     * @param function
     */
    public void selectSubscribeResolution(Context context, Function<Point> function) {
        if (context == null) return;
        position = 0;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(context);
        singleChoiceDialog.setTitle(context.getString(R.string.resolution_list));
        ArrayList<Point> resolutionList = Constant.getResolutionList();
        String[] items = new String[resolutionList.size()];
        for (int i = 0; i < resolutionList.size(); i++) {
            Point point = resolutionList.get(i);
            items[i] = String.valueOf(point.x + " x " + point.y);
        }
        singleChoiceDialog.setSingleChoiceItems(items, position, (dialog, which) -> {
            position = which;
        });

        singleChoiceDialog.setPositiveButton("ok", (dialog, which) -> {
            if (function != null) {
                Point point = resolutionList.get(position);
                function.action(point);
            }
        });
        singleChoiceDialog.show();
    }

    /**
     * 帧率
     *
     * @param context
     * @param function
     */
    public void selectFramerate(Context context, Function<String> function) {
        if (context == null) return;
        position = 0;
        ArrayList<Integer> framerateList = Constant.getFramerateList();
        String[] items = new String[framerateList.size()];
        int selectFramerate = getFramerate();
        for (int i = 0; i < framerateList.size(); i++) {
            Integer framerate = framerateList.get(i);
            items[i] = String.valueOf(framerate);
            if (selectFramerate == framerate) {
                position = i;
            }
        }
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(context);
        singleChoiceDialog.setTitle(context.getString(R.string.framerate_list));
        singleChoiceDialog.setSingleChoiceItems(items, position, (dialog, which) -> {
            position = which;
        });
        singleChoiceDialog.setPositiveButton("ok", (dialog, which) -> {
            if (function != null) {
                Integer framerate = framerateList.get(position);
                function.action(String.valueOf(framerate));
                setFramerate(framerate);
            }
        });
        singleChoiceDialog.show();
    }

    /**
     * 远端流
     *
     * @param context
     * @param function
     */
    public void selectRemoteStream(Context context, ArrayList<String> remoteStreamList, Function2<String, Integer> function) {
        if (context == null) return;
        position = 0;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(context);
        singleChoiceDialog.setTitle(context.getString(R.string.remote_stream_list));
        String[] items = (String[]) remoteStreamList.toArray(new String[0]);
        singleChoiceDialog.setSingleChoiceItems(items, position, (dialog, which) -> {
            position = which;
        });

        singleChoiceDialog.setPositiveButton("ok", (dialog, which) -> {
            if (function != null) {
                String remoteID = remoteStreamList.get(position);
                function.action(remoteID, position);
            }
        });
        singleChoiceDialog.show();
    }

    // camera true 前置 false 后置
    public void setCameraFront(boolean isFront) {
        SharePreferences.getInstance().setCameraFront(isFront);
    }

    public Boolean getCameraFront() {
        return SharePreferences.getInstance().getCameraFront();
    }

    // 设置 ServerUrl
    public void setServerUrl(String serverUrl) {
        SharePreferences.getInstance().setServerUrl(serverUrl);
    }

    public String getServerUrl() {
        return SharePreferences.getInstance().getServerUrl();
    }

    // 设置 roomID
    public void setRoomID(String roomid) {
        SharePreferences.getInstance().setRoomID(roomid);
    }

    public String getRoomID() {
        return SharePreferences.getInstance().getRoomID();
    }

    // 设置 Resolution
    public void setResolution(Point resolution) {
        SharePreferences.getInstance().setResolution(resolution);
    }

    public Point getResolution() {
        return SharePreferences.getInstance().getResolution();
    }

    // 设置 Framerate
    public void setFramerate(int framerate) {
        SharePreferences.getInstance().setFramerate(framerate);
    }

    public int getFramerate() {
        return SharePreferences.getInstance().getFramerate();
    }

    // 设置 codec
    public void setCodec(String codec) {
        SharePreferences.getInstance().setCodec(codec);
    }

    public String getCodec() {
        return SharePreferences.getInstance().getCodec();
    }
}
