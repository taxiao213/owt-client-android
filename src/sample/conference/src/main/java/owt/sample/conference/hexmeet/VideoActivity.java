package owt.sample.conference.hexmeet;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.webrtc.PeerConnection;
import org.webrtc.RTCStatsReport;
import org.webrtc.SurfaceViewRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import owt.base.ActionCallback;
import owt.base.AudioCodecParameters;
import owt.base.ContextInitialization;
import owt.base.LocalStream;
import owt.base.MediaCodecs;
import owt.base.MediaConstraints;
import owt.base.OwtError;
import owt.base.VideoCodecParameters;
import owt.base.VideoEncodingParameters;
import owt.conference.ConferenceClient;
import owt.conference.ConferenceClientConfiguration;
import owt.conference.ConferenceInfo;
import owt.conference.Participant;
import owt.conference.Publication;
import owt.conference.PublicationSettings;
import owt.conference.PublishOptions;
import owt.conference.RemoteStream;
import owt.conference.SubscribeOptions;
import owt.conference.Subscription;
import owt.conference.SubscriptionCapabilities;
import owt.sample.conference.HttpUtils;
import owt.sample.conference.R;
import owt.sample.conference.hexmeet.utils.Function;
import owt.sample.conference.hexmeet.utils.Function2;
import owt.sample.conference.hexmeet.utils.LogUtils;
import owt.sample.conference.hexmeet.utils.ToastUtils;
import owt.sample.conference.hexmeet.utils.UIUtils;
import owt.sample.utils.OwtScreenCapturer;
import owt.sample.utils.OwtVideoCapturer;

import static org.webrtc.PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
import static owt.base.MediaCodecs.AudioCodec.OPUS;
import static owt.base.MediaCodecs.AudioCodec.PCMU;
import static owt.base.MediaCodecs.VideoCodec.H264;
import static owt.base.MediaCodecs.VideoCodec.H265;
import static owt.base.MediaCodecs.VideoCodec.VP8;
import static owt.base.MediaCodecs.VideoCodec.VP9;

/**
 * Created by hanqq on 2020/11/30
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class VideoActivity extends AppCompatActivity implements VideoFragment2.VideoFragmentListener, ActivityCompat.OnRequestPermissionsResultCallback, ConferenceClient.ConferenceClientObserver, View.OnClickListener {
    private final String TAG = "HEX_VideoActivity";
    private final int OWT_REQUEST_CODE = 100;
    private boolean contextHasInitialized = false;
    public EglBase rootEglBase;
    private boolean fullScreen = false;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Timer statsTimer;
    private VideoFragment2 videoFragment;
    private Button disconnectBtn, sharescreenBtn, publishBtn, subscribeBtn;
    private ConferenceClient conferenceClient;
    private ConferenceInfo conferenceInfo;
    private Publication publication;
    private Subscription subscription;
    private LocalStream localStream;
    private OwtVideoCapturer capturer;
    private LocalStream screenStream;
    private OwtScreenCapturer screenCapturer;
    private Publication screenPublication;
    private SurfaceViewRenderer localRenderer;
    private RemoteStream remoteForwardStream = null;
    private int subscribeSimulcastRidChoice = 0;
    private ArrayList<String> remoteStreamIdList = new ArrayList<>();
    private HashMap<String, RemoteStream> remoteStreamMap = new HashMap<>();
    private HashMap<String, List<String>> videoCodecMap = new HashMap<>();
    private HashMap<String, List<String>> simulcastStreamMap = new HashMap<>();
    private String token;
    private ArrayList<SurfaceViewRenderer> rendererArrayList;
    private FragmentTransaction transaction;
    public int currentRender = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_video);

        transaction = getSupportFragmentManager().beginTransaction();
        videoFragment = new VideoFragment2();
        videoFragment.setListener(this);
        transaction.replace(R.id.fl, videoFragment);
        transaction.commitAllowingStateLoss();

        disconnectBtn = findViewById(R.id.multi_func_btn_disconnect);
        sharescreenBtn = findViewById(R.id.multi_func_btn_sharescreen);
        publishBtn = findViewById(R.id.multi_func_btn_publish);
        subscribeBtn = findViewById(R.id.multi_func_btn_subscribe);
        disconnectBtn.setOnClickListener(this);
        sharescreenBtn.setOnClickListener(this);
        publishBtn.setOnClickListener(this);
        subscribeBtn.setOnClickListener(this);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        }

        Intent intent = getIntent();
        if (intent != null) {
            token = intent.getStringExtra(Constant.ACTION_INTENT_PARAMETER1);
        }
        initConferenceClient();
        if (!TextUtils.isEmpty(token)) {
            conferenceClient.join(token, new ActionCallback<ConferenceInfo>() {
                @Override
                public void onSuccess(ConferenceInfo conferenceInfo) {
                    VideoActivity.this.conferenceInfo = conferenceInfo;
                    for (RemoteStream remoteStream : conferenceInfo.getRemoteStreams()) {
                        remoteStreamIdList.add(remoteStream.id());
                        remoteStreamMap.put(remoteStream.id(), remoteStream);
                        getParameterByRemoteStream(remoteStream);
                        remoteStream.addObserver(new owt.base.RemoteStream.StreamObserver() {
                            @Override
                            public void onEnded() {
                                remoteStreamIdList.remove(remoteStream.id());
                                remoteStreamMap.remove(remoteStream.id());
                            }

                            @Override
                            public void onUpdated() {

                            }
                        });
                    }
                    startTimer();
                }

                @Override
                public void onFailure(OwtError e) {
                    runOnUiThread(() -> {

                    });
                }
            });
        }
    }

    private void startTimer() {
        if (statsTimer != null) {
            statsTimer.cancel();
            statsTimer = null;
        }
        statsTimer = new Timer();
        statsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getStats();
            }
        }, 0, Constant.STATS_INTERVAL_MS);
    }

    private void getStats() {
        if (publication != null) {
            publication.getStats(new ActionCallback<RTCStatsReport>() {
                @Override
                public void onSuccess(RTCStatsReport result) {
                    videoFragment.updateStats(result, true);
                }

                @Override
                public void onFailure(OwtError error) {

                }
            });
        }
        if (screenPublication != null) {
            screenPublication.getStats(new ActionCallback<RTCStatsReport>() {
                @Override
                public void onSuccess(RTCStatsReport result) {
                    videoFragment.updateStats(result, true);
                }

                @Override
                public void onFailure(OwtError error) {

                }
            });
        }
        if (subscription != null) {
            subscription.getStats(new ActionCallback<RTCStatsReport>() {
                @Override
                public void onSuccess(RTCStatsReport result) {
                    videoFragment.updateStats(result, false);
                }

                @Override
                public void onFailure(OwtError error) {

                }
            });
        }
    }

    private void initConferenceClient() {
        rootEglBase = EglBase.create();
        if (!contextHasInitialized) {
            ContextInitialization.create()
                    .setApplicationContext(this)
                    .addIgnoreNetworkType(ContextInitialization.NetworkType.LOOPBACK)
                    .setVideoHardwareAccelerationOptions(
                            rootEglBase.getEglBaseContext(),
                            rootEglBase.getEglBaseContext())
                    .initialize();
            contextHasInitialized = true;
        }

        PeerConnection.IceServer iceServer = PeerConnection.IceServer.builder(
                "turn:example.com?transport=tcp").setUsername("userName").setPassword(
                "passward").createIceServer();
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(iceServer);
        PeerConnection.RTCConfiguration rtcConfiguration = new PeerConnection.RTCConfiguration(
                iceServers);
        HttpUtils.setUpINSECURESSLContext();
        rtcConfiguration.continualGatheringPolicy = GATHER_CONTINUALLY;

        rtcConfiguration.enableCpuOveruseDetection = false;
        ConferenceClientConfiguration configuration
                = ConferenceClientConfiguration.builder()
                .setHostnameVerifier(HttpUtils.hostnameVerifier)
                .setSSLContext(HttpUtils.sslContext)
                .setRTCConfiguration(rtcConfiguration)
                .build();
        conferenceClient = new ConferenceClient(configuration);
        conferenceClient.addObserver(this);
    }

    public void getParameterByRemoteStream(RemoteStream remoteStream) {
        List<String> videoCodecList = new ArrayList<>();
        List<String> ridList = new ArrayList<>();
        SubscriptionCapabilities.VideoSubscriptionCapabilities videoSubscriptionCapabilities
                = remoteStream.extraSubscriptionCapability.videoSubscriptionCapabilities;
        for (VideoCodecParameters videoCodec : videoSubscriptionCapabilities.videoCodecs) {
            videoCodecList.add(videoCodec.name.name());
            videoCodecMap.put(remoteStream.id(), videoCodecList);
        }

        for (PublicationSettings.VideoPublicationSettings videoPublicationSetting :
                remoteStream.publicationSettings.videoPublicationSettings) {
            if (videoCodecMap.containsKey(remoteStream.id())) {
                videoCodecMap.get(remoteStream.id()).add(videoPublicationSetting.codec.name.name());
            } else {
                videoCodecList.add(videoPublicationSetting.codec.name.name());
                videoCodecMap.put(remoteStream.id(), videoCodecList);
            }

            if (videoPublicationSetting.rid != null) {
                ridList.add(videoPublicationSetting.rid);
            }
        }

        if (ridList.size() != 0) {
            simulcastStreamMap.put(remoteStream.id(), ridList);
        }
    }


    private void disconnect() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                conferenceClient.leave();
                conferenceClient = null;
                clean();
                finish();
            }
        });

    }

    private void clean() {
        try {
            if (statsTimer != null) {
                statsTimer.cancel();
                statsTimer = null;
            }
            publication.stop();
            publication = null;
            subscription.stop();
            subscription = null;
            if (localStream != null) {
                localStream.detach(localRenderer);
                localStream.dispose();
                localStream = null;
            }
            if (capturer != null) {
                capturer.stopCapture();
                capturer.dispose();
                capturer = null;
            }
            remoteStreamIdList.clear();
            remoteStreamMap.clear();
            videoCodecMap.clear();
            simulcastStreamMap.clear();
            localRenderer.release();
            for (int i = 0; i < rendererArrayList.size(); i++) {
                SurfaceViewRenderer surfaceViewRenderer = rendererArrayList.get(i);
                if (surfaceViewRenderer != null) {
                    surfaceViewRenderer.release();
                }
            }
            rendererArrayList.clear();
            videoFragment = null;
            rootEglBase.release();
            rootEglBase = null;
            ContextInitialization.destory();
        } catch (Exception e) {
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sharescreen() {
        if (sharescreenBtn.getText().equals("ShareScreen")) {
            MediaProjectionManager manager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            startActivityForResult(manager.createScreenCaptureIntent(), OWT_REQUEST_CODE);
        } else {
            executor.execute(() -> {
                if (screenPublication != null) {
                    screenPublication.stop();
                    screenPublication = null;
                }
            });
            sharescreenBtn.setEnabled(true);
            sharescreenBtn.setTextColor(Color.WHITE);
            sharescreenBtn.setText(R.string.share_screen);
        }
    }

    private void publish() {
        executor.execute(() -> {
            Point resolution = UIUtils.getInstance().getResolution();
            boolean isCameraFront = UIUtils.getInstance().getCameraFront();
            capturer = OwtVideoCapturer.create(resolution.x, resolution.y, UIUtils.getInstance().getFramerate(), true, isCameraFront);
            LogUtils.d(TAG, " resolution: " + resolution.toString() + " framerate: " + UIUtils.getInstance().getFramerate());
            localStream = new LocalStream(capturer, new MediaConstraints.AudioTrackConstraints());
            localStream.attach(localRenderer);

            ActionCallback<Publication> callback = new ActionCallback<Publication>() {
                @Override
                public void onSuccess(final Publication result) {
                    runOnUiThread(() -> {
                        localRenderer.setVisibility(View.VISIBLE);
                    });
                    publication = result;
                    try {
                        JSONArray mixBody = new JSONArray();
                        JSONObject body = new JSONObject();
                        body.put("op", "add");
                        body.put("path", "/info/inViews");
                        body.put("value", "common");
                        mixBody.put(body);

                        String serverUrl = Constant.BASE_URL;
                        String uri = serverUrl
                                + "/rooms/" + conferenceInfo.id()
                                + "/streams/" + result.id();
                        HttpUtils.request(uri, "PATCH", mixBody.toString(), true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(final OwtError error) {
                    runOnUiThread(() -> {
                        ToastUtils.show("Failed to publish " + error.errorMessage);
                    });

                }
            };

            conferenceClient.publish(localStream, setPublishOptions(), callback);
        });
    }

    public PublishOptions setPublishOptions() {
        VideoEncodingParameters h264 = new VideoEncodingParameters(MediaCodecs.VideoCodec.get(UIUtils.getInstance().getCodec()));
        PublishOptions options = PublishOptions.builder()
                .addVideoParameter(h264)
                .build();
        return options;
    }

    private void subscribe() {
        subscribeSimulcastRidChoice = 0;

        // TODO: 2020/11/18 新增
        for (int i = 0; i < remoteStreamIdList.size(); i++) {
            String id = remoteStreamIdList.get(i);
            if (id.endsWith("-common"))
                remoteStreamIdList.remove(id);
        }

        UIUtils.getInstance().selectRemoteStream(VideoActivity.this, remoteStreamIdList, new Function2<String, Integer>() {
            @Override
            public void action(String streamID, Integer chooseItem) {
                if (streamID != null && chooseItem != null) {
                    chooseCodec(remoteStreamMap.get(streamID), chooseItem);
                }
            }
        });
    }

    public void chooseCodec(RemoteStream remoteStream, int selectid) {
        List<String> videoCodecList = videoCodecMap.get(remoteStream.id());
        removeDuplicate(videoCodecList);
        String chooseVideoCodec = UIUtils.getInstance().getCodec();
        if (simulcastStreamMap.containsKey(remoteStream.id())) {
            chooseRid(remoteStream, chooseVideoCodec, selectid);
        } else {
            subscribeForward(remoteStream, chooseVideoCodec, null, selectid);
        }
    }

    public void subscribeForward(RemoteStream remoteStream, String videoCodec, String rid, int selectid) {
        UIUtils.getInstance().selectSubscribeResolution(VideoActivity.this, new Function<Point>() {
            @Override
            public void action(Point var) {
                if (var != null) {
                    SubscribeOptions.VideoSubscriptionConstraints.Builder videoOptionBuilder = SubscribeOptions.VideoSubscriptionConstraints.builder();
                    VideoCodecParameters vcp = new VideoCodecParameters(MediaCodecs.VideoCodec.get(UIUtils.getInstance().getCodec()));
                    if (rid != null) {
                        videoOptionBuilder.setRid(rid);
                    }
                    SubscribeOptions.VideoSubscriptionConstraints videoOption = videoOptionBuilder
                            .addCodec(vcp)
                            .setResolution(var.x, var.y)
                            .build();

                    SubscribeOptions.AudioSubscriptionConstraints audioOption =
                            SubscribeOptions.AudioSubscriptionConstraints.builder()
                                    .addCodec(new AudioCodecParameters(OPUS))
                                    .addCodec(new AudioCodecParameters(PCMU))
                                    .build();

                    SubscribeOptions options = SubscribeOptions.builder(true, true)
                            .setAudioOption(audioOption)
                            .setVideoOption(videoOption)
                            .build();

                    conferenceClient.subscribe(remoteStream, options,
                            new ActionCallback<Subscription>() {
                                @Override
                                public void onSuccess(Subscription result) {
                                    VideoActivity.this.subscription = result;
                                    VideoActivity.this.remoteForwardStream = remoteStream;
//                                    if (selectid >= (remoteStreamIdList.size())) {
//                                        remoteStream.attach(rendererArrayList.get(rendererArrayList.size()));
//                                    } else {
//                                        remoteStream.attach(rendererArrayList.get(selectid + 1));
//                                    }

                                    if (currentRender < 1) {
                                        currentRender = 1;
                                    } else if (currentRender == rendererArrayList.size()) {
                                        currentRender = rendererArrayList.size() - 1;
                                    }
                                    remoteStream.attach(rendererArrayList.get(currentRender));
                                    currentRender = currentRender + 1;
                                }

                                @Override
                                public void onFailure(OwtError error) {
                                    Log.e(TAG, "Failed to subscribe " + error.errorMessage);
                                    ToastUtils.show("Failed to subscribe " + error.errorMessage);
                                }
                            });
                }
            }
        });


    }

    public void chooseRid(RemoteStream remoteStream, String videoCodec, int selectid) {
        List<String> ridList = simulcastStreamMap.get(remoteStream.id());
        removeDuplicate(ridList);
        final String[] items = (String[]) ridList.toArray(new String[0]);
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(VideoActivity.this);
        singleChoiceDialog.setTitle("Rid List");
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                (dialog, which) -> subscribeSimulcastRidChoice = which);
        singleChoiceDialog.setPositiveButton("ok",
                (dialog, which) -> subscribeForward(remoteStream, videoCodec, items[subscribeSimulcastRidChoice], selectid));
        singleChoiceDialog.show();
    }

    public void removeDuplicate(List<String> list) {
        LinkedHashSet<String> set = new LinkedHashSet<String>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
    }

    @Override
    protected void onPause() {
        if (localStream != null) {
            localStream.detach(localRenderer);
        }
        /*if (stream2Sub != null) {
            stream2Sub.detach(remoteRenderer);
        }*/

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (localStream != null) {
            localStream.attach(localRenderer);
        }
       /* if (stream2Sub != null) {
            stream2Sub.attach(remoteRenderer);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                clean();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        screenCapturer = new OwtScreenCapturer(data, 1280, 720);
        screenStream = new LocalStream(screenCapturer,
                new MediaConstraints.AudioTrackConstraints());

        executor.execute(
                () -> conferenceClient.publish(screenStream, setPublishOptions(),
                        new ActionCallback<Publication>() {
                            @Override
                            public void onSuccess(Publication result) {
                                runOnUiThread(() -> {
                                    sharescreenBtn.setEnabled(true);
                                    sharescreenBtn.setTextColor(Color.WHITE);
                                    sharescreenBtn.setText(R.string.stop_screen);
                                });
                                screenPublication = result;
                            }

                            @Override
                            public void onFailure(OwtError error) {
                                runOnUiThread(() -> {
                                    sharescreenBtn.setEnabled(true);
                                    sharescreenBtn.setTextColor(Color.WHITE);
                                    sharescreenBtn.setText(R.string.share_screen);
                                });
                                screenCapturer.stopCapture();
                                screenCapturer.dispose();
                                screenCapturer = null;
                                screenStream.dispose();
                                screenStream = null;
                            }
                        }));
    }

    @Override
    public void onStreamAdded(RemoteStream remoteStream) {
        String id = remoteStream.id();
        remoteStreamIdList.add(id);
        remoteStreamMap.put(id, remoteStream);
        getParameterByRemoteStream(remoteStream);
        remoteStream.addObserver(new owt.base.RemoteStream.StreamObserver() {
            @Override
            public void onEnded() {
                remoteStreamIdList.remove(id);
                remoteStreamMap.remove(id);
            }

            @Override
            public void onUpdated() {
                getParameterByRemoteStream(remoteStream);
            }
        });
    }

    @Override
    public void onParticipantJoined(Participant participant) {

    }

    @Override
    public void onMessageReceived(String message, String from, String to) {

    }

    @Override
    public void onServerDisconnected() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                clean();
            }
        });
    }

    @Override
    public void onRenderer(SurfaceViewRenderer localRenderer, SurfaceViewRenderer remoteRenderer) {

    }

    @Override
    public void onRendererList(ArrayList<SurfaceViewRenderer> list) {
        this.rendererArrayList = list;
        this.localRenderer = rendererArrayList.get(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.multi_func_btn_disconnect:
                disconnect();
                break;
            case R.id.multi_func_btn_sharescreen:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sharescreen();
                }
                break;
            case R.id.multi_func_btn_publish:
                publish();
                break;
            case R.id.multi_func_btn_subscribe:
                subscribe();
                break;
        }
    }

}
