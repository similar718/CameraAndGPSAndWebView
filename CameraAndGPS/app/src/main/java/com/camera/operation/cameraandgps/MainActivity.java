package com.camera.operation.cameraandgps;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.camera.operation.cameraandgps.ui.CameraGPSActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mCameraBtn;
    private Button mControlBtn;
    private Button mMessageBtn;
    private Button mMeBtn;
    private Button mSettingBtn;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        mCameraBtn = (Button) findViewById(R.id.main_camera_btn);
        mControlBtn = (Button) findViewById(R.id.main_control_btn);
        mMessageBtn = (Button) findViewById(R.id.main_message_btn);
        mMeBtn = (Button) findViewById(R.id.main_me_btn);
        mSettingBtn = (Button) findViewById(R.id.main_setting_btn);

        mCameraBtn.setOnClickListener(MainActivity.this);
        mControlBtn.setOnClickListener(MainActivity.this);
        mMessageBtn.setOnClickListener(MainActivity.this);
        mMeBtn.setOnClickListener(MainActivity.this);
        mSettingBtn.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_camera_btn://GPS拍照
                Intent cameraIntent = new Intent(MainActivity.this, CameraGPSActivity.class);
                startActivity(cameraIntent);
                break;
        case R.id.main_control_btn://监控管理

                break;
        case R.id.main_message_btn://公告通知

                break;
        case R.id.main_me_btn://我的任务

                break;
        case R.id.main_setting_btn://设置

                break;
        default:
            break;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
