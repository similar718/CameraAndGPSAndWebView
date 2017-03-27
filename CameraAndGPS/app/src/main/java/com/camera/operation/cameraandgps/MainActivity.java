package com.camera.operation.cameraandgps;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.camera.operation.cameraandgps.base.LocalEntity;
import com.camera.operation.cameraandgps.ui.CameraGPSActivity;
import com.camera.operation.cameraandgps.ui.ControlActivity;
import com.camera.operation.cameraandgps.ui.MeActivity;
import com.camera.operation.cameraandgps.ui.MessageActivity;
import com.camera.operation.cameraandgps.ui.SettingActivity;
import com.camera.operation.cameraandgps.util.Constants;
import com.camera.operation.cameraandgps.util.DbHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mCameraBtn;
    private Button mControlBtn;
    private Button mMessageBtn;
    private Button mMeBtn;
    private Button mSettingBtn;

    private DbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mHelper = new DbHelper(this);

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

        requestFileToTest();
    }

    private void requestFileToTest(){

        List<LocalEntity> entityList = null;
        entityList = mHelper.query();
        if (entityList.size()>0) {
            Constants.mControlUrl = entityList.get(entityList.size()-1).mControlUrl;
            Constants.mMessageUrl = entityList.get(entityList.size()-1).mMessageUrl;
            Constants.mMeUrl = entityList.get(entityList.size()-1).mMeUrl;
//            Log.e("ooooooooooo","mControlUrl"+Constants.mControlUrl+"  Constants.mMessageUrl"+Constants.mMessageUrl+"  Constants.mMeUrl"+Constants.mMeUrl+" list size"+entityList.size());
            if (entityList.size()-1>0){
                for (int i=0;i<entityList.size()-1;i++){
                    mHelper.delete(entityList.get(i).mControlUrl);
                }
            }
//            Log.e("oooooooooo","list size = "+mHelper.query().size());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_camera_btn://GPS拍照
                //requestCameraPictureSize();
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
}
