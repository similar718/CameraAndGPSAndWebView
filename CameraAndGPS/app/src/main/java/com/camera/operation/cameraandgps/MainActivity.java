package com.camera.operation.cameraandgps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.camera.operation.cameraandgps.ui.CameraGPSActivity;
import com.camera.operation.cameraandgps.ui.ControlActivity;
import com.camera.operation.cameraandgps.ui.MeActivity;
import com.camera.operation.cameraandgps.ui.MessageActivity;
import com.camera.operation.cameraandgps.ui.SettingActivity;
import com.camera.operation.cameraandgps.util.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mCameraBtn;
    private Button mControlBtn;
    private Button mMessageBtn;
    private Button mMeBtn;
    private Button mSettingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
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

        Constants.mControlUrl = "".equals(readNeedFile(Constants.NeedPathControl))?Constants.mControlUrl:readNeedFile(Constants.NeedPathControl);
        Constants.mMessageUrl = "".equals(readNeedFile(Constants.NeedPathMessage))?Constants.mMessageUrl:readNeedFile(Constants.NeedPathMessage);
        Constants.mMeUrl = "".equals(readNeedFile(Constants.NeedPathMe))?Constants.mMeUrl:readNeedFile(Constants.NeedPathMe);
        Toast.makeText(MainActivity.this,Constants.mControlUrl+"\n"+Constants.mMessageUrl+"\n"+Constants.mMeUrl,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_camera_btn://GPS拍照
                Intent cameraIntent = new Intent(MainActivity.this, CameraGPSActivity.class);
                startActivity(cameraIntent);
                break;
            case R.id.main_control_btn://监控管理
                Intent controlIntent = new Intent(MainActivity.this, ControlActivity.class);
                startActivity(controlIntent);
                break;
            case R.id.main_message_btn://公告通知
                Intent messageIntent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(messageIntent);
                break;
            case R.id.main_me_btn://我的任务
                Intent meIntent = new Intent(MainActivity.this, MeActivity.class);
                startActivity(meIntent);
                break;
            case R.id.main_setting_btn://设置
                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(settingIntent);
                break;
            default:
                break;
        }
    }

    private String readNeedFile(String filepath){
        String str = "";
        try {
            File file = new File(Constants.SysFilePhotoPathNeed);
            if (!file.exists())
                file.mkdir();
            File urlFile = new File(filepath);
            if (urlFile == null)
                return "";
            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String mimeTypeLine = null ;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str+mimeTypeLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

}
