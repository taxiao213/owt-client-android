package owt.sample.conference.hexmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

import owt.sample.conference.HttpUtils;
import owt.sample.conference.R;
import owt.sample.conference.hexmeet.utils.Function;
import owt.sample.conference.hexmeet.utils.UIUtils;
import owt.sample.conference.hexmeet.utils.XXPermissionsUtils;


/**
 * Created by hanqq on 2020/11/30
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_hexmeet_main);
        findViewById(R.id.multi_func_btn_connect).setOnClickListener(this);
        findViewById(R.id.multi_func_btn_setting).setOnClickListener(this);

        UIUtils.getInstance().setCameraFront(true);
        UIUtils.getInstance().setServerUrl(Constant.BASE_URL);
        UIUtils.getInstance().setRoomID(Constant.ROOM_ID);
        UIUtils.getInstance().setResolution(Constant.RESOLUTION_DEFAULT);
        UIUtils.getInstance().setFramerate(Constant.FRAMERATE_DEFAULT);
        UIUtils.getInstance().setCodec(Constant.VIDEOE_CODEC_DEFAULT);
    }

    /**
     * 连接
     */
    private void connect() {
        HttpUtils.setUpINSECURESSLContext();
        Executors.newSingleThreadExecutor().execute(() -> {
            String serverUrl = UIUtils.getInstance().getServerUrl();
            String roomId = UIUtils.getInstance().getRoomID();

            JSONObject joinBody = new JSONObject();
            try {
                joinBody.put("role", "presenter");
                joinBody.put("username", "user");
                joinBody.put("room", roomId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String uri = serverUrl + "/createToken/";
            String token = HttpUtils.request(uri, "POST", joinBody.toString(), true);

            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            intent.putExtra(Constant.ACTION_INTENT_PARAMETER1, token);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.multi_func_btn_connect:
                XXPermissionsUtils.getInstances().hasCameraPermission(new Function<Boolean>() {
                    @Override
                    public void action(Boolean var) {
                        connect();
                    }
                }, MainActivity.this);
                break;
            case R.id.multi_func_btn_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;

        }
    }
}
