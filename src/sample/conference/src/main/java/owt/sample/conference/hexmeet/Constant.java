package owt.sample.conference.hexmeet;

import android.graphics.Point;
import android.media.MediaCodec;

import java.util.ArrayList;

import owt.base.MediaCodecs;

/**
 * Created by hanqq on 2020/11/30
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class Constant {

    public static final String SHARE_PREFERENCE_NAME = "hex_meet";
    public static final boolean LOG_SWITCH = true;

    public static String BASE_URL = "https://192.168.1.107:3004";
    public static String ROOM_ID = "5fb3901c4367ee1abb650c8e";
    // 默认前置摄像头
    public static boolean CAMERA_FRONT = true;

    public static final int STATS_INTERVAL_MS = 5000;
    private static final int OWT_REQUEST_CODE = 100;

    // 分辨率
    // cif: 352x288
    // vga: 640x480
    // svga:800x600
    // xga:1024x768
    // r640x360:640x360
    // hd720p:1280x720
    // sif:320x240
    // hvga:480x320
    // r480x360:480x360
    // qcif:176x144
    // r192x144:192x144
    // hd1080p:1920x1080
    // uhd_4k:3840x2160
    // r360x360:360x360
    // r480x480:480x480
    // r720x720:720x720
    // r720x1280:720x1080
    // r1080x1920:1080x1920
    // r320x180:320x180
    // r160x120:160x120

    public static final Point RESOLUTION_R160x120 = new Point(160, 120);
    public static final Point RESOLUTION_QCIF = new Point(176, 144);
    public static final Point RESOLUTION_R192x144 = new Point(192, 144);
    public static final Point RESOLUTION_R320x180 = new Point(320, 180);
    public static final Point RESOLUTION_SIF = new Point(320, 240);
    public static final Point RESOLUTION_CIF = new Point(352, 288);
    public static final Point RESOLUTION_R360x360 = new Point(360, 360);
    public static final Point RESOLUTION_HVGA = new Point(480, 320);
    public static final Point RESOLUTION_R480x480 = new Point(480, 480);
    public static final Point RESOLUTION_R480x360 = new Point(480, 360);
    public static final Point RESOLUTION_R640x360 = new Point(640, 360);
    public static final Point RESOLUTION_VGA = new Point(640, 480);
    public static final Point RESOLUTION_R720x720 = new Point(720, 720);
    public static final Point RESOLUTION_R720x1280 = new Point(720, 1080);
    public static final Point RESOLUTION_SVGA = new Point(800, 600);
    public static final Point RESOLUTION_XGA = new Point(1024, 768);
    public static final Point RESOLUTION_R1080x1920 = new Point(1080, 1920);
    public static final Point RESOLUTION_HD720P = new Point(1280, 720);
    public static final Point RESOLUTION_HD1080p = new Point(1920, 1080);
    public static final Point RESOLUTION_UHD_4K = new Point(3840, 2160);

    // 默认分辨率
    public static final Point RESOLUTION_DEFAULT = RESOLUTION_HD720P;

    public static ArrayList<Point> getResolutionList() {
        ArrayList<Point> resolutionList = new ArrayList<>();
        resolutionList.add(Constant.RESOLUTION_R160x120);
        resolutionList.add(Constant.RESOLUTION_QCIF);
        resolutionList.add(Constant.RESOLUTION_R192x144);
        resolutionList.add(Constant.RESOLUTION_R320x180);
        resolutionList.add(Constant.RESOLUTION_SIF);
        resolutionList.add(Constant.RESOLUTION_CIF);
        resolutionList.add(Constant.RESOLUTION_R360x360);
        resolutionList.add(Constant.RESOLUTION_HVGA);
        resolutionList.add(Constant.RESOLUTION_R480x480);
        resolutionList.add(Constant.RESOLUTION_R480x360);
        resolutionList.add(Constant.RESOLUTION_R640x360);
        resolutionList.add(Constant.RESOLUTION_VGA);
        resolutionList.add(Constant.RESOLUTION_R720x720);
        resolutionList.add(Constant.RESOLUTION_R720x1280);
        resolutionList.add(Constant.RESOLUTION_SVGA);
        resolutionList.add(Constant.RESOLUTION_XGA);
        resolutionList.add(Constant.RESOLUTION_R1080x1920);
        resolutionList.add(Constant.RESOLUTION_HD720P);
        resolutionList.add(Constant.RESOLUTION_HD1080p);
        resolutionList.add(Constant.RESOLUTION_UHD_4K);
        return resolutionList;
    }

    // 帧率
    public static final int FRAMERATE_6 = 6;
    public static final int FRAMERATE_12 = 12;
    public static final int FRAMERATE_15 = 15;
    public static final int FRAMERATE_24 = 24;
    public static final int FRAMERATE_25 = 25;
    public static final int FRAMERATE_30 = 30;
    public static final int FRAMERATE_48 = 48;
    public static final int FRAMERATE_60 = 60;

    // 默认帧率
    public static final int FRAMERATE_DEFAULT = FRAMERATE_25;

    public static ArrayList<Integer> getFramerateList() {
        ArrayList<Integer> framerateList = new ArrayList<>();
        framerateList.add(Constant.FRAMERATE_6);
        framerateList.add(Constant.FRAMERATE_12);
        framerateList.add(Constant.FRAMERATE_15);
        framerateList.add(Constant.FRAMERATE_24);
        framerateList.add(Constant.FRAMERATE_25);
        framerateList.add(Constant.FRAMERATE_30);
        framerateList.add(Constant.FRAMERATE_48);
        framerateList.add(Constant.FRAMERATE_60);
        return framerateList;
    }

    // 默认264
    public static String VIDEOE_CODEC_DEFAULT = MediaCodecs.VideoCodec.H264.name();

    public static final String ACTION_INTENT_BUNDLE = "action_intent_bundle";
    public static final String ACTION_INTENT_PARAMETER1 = "action_intent_parameter1";
    public static final String ACTION_INTENT_PARAMETER2 = "action_intent_parameter2";
    public static final String ACTION_INTENT_PARAMETER3 = "action_intent_parameter3";
    public static final String ACTION_INTENT_PARAMETER4 = "action_intent_parameter4";
}
