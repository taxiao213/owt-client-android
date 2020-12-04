package owt.sample.conference.hexmeet;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import owt.sample.conference.R;
import owt.sample.conference.hexmeet.utils.Function;
import owt.sample.conference.hexmeet.utils.UIUtils;

/**
 * Created by hanqq on 2020/12/2
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etServerUrl;
    private EditText etRoomId;
    private RadioButton front;
    private RadioButton back;
    private RadioButton pub_h264;
    private TextView tv_resolution;
    private TextView tv_framerate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        etServerUrl = findViewById(R.id.server_url);
        etRoomId = findViewById(R.id.room_id);
        front = findViewById(R.id.front);
        back = findViewById(R.id.back);
        pub_h264 = findViewById(R.id.pub_h264);
        tv_resolution = findViewById(R.id.tv_resolution);
        tv_framerate = findViewById(R.id.tv_framerate);
        tv_resolution.setOnClickListener(this);
        tv_framerate.setOnClickListener(this);
        pub_h264.setChecked(true);

        etServerUrl.setText(UIUtils.getInstance().getServerUrl());
        etRoomId.setText(UIUtils.getInstance().getRoomID());
        Boolean cameraFront = UIUtils.getInstance().getCameraFront();
        if (cameraFront) {
            front.setChecked(true);
        } else {
            back.setChecked(true);
        }
        Point resolution = UIUtils.getInstance().getResolution();
        tv_resolution.setText(resolution.x + " x " + resolution.y);
        tv_framerate.setText(String.valueOf(UIUtils.getInstance().getFramerate()));
    }

    private void selectResolution() {
        UIUtils.getInstance().selectResolution(SettingActivity.this, var -> {
            if (var != null) {
                tv_resolution.setText(var.x + " x " + var.y);
            }
        });
    }

    private void selectFramerate() {
        UIUtils.getInstance().selectFramerate(SettingActivity.this, var -> {
            if (var != null) {
                tv_framerate.setText(var);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_resolution:
                selectResolution();
                break;
            case R.id.tv_framerate:
                selectFramerate();
                break;
        }
    }

}
