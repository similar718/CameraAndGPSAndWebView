package com.camera.operation.cameraandgps.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.camera.operation.cameraandgps.R;
import com.camera.operation.cameraandgps.util.Constants;
import com.camera.operation.cameraandgps.view.TopbarView;

/**
 * Created by similar on 2017/2/10.
 */

public class SettingActivity extends AppCompatActivity {
    private TopbarView mTopBar;
    private ImageView mLeft;

    private RelativeLayout mSettingUpdate;

    private ToggleButton mControlTB;
    private ToggleButton mMessageTB;
    private ToggleButton mMeTB;

    //private SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView(){
        //sp = getSharedPreferences("config", Context.MODE_PRIVATE);

        mTopBar = (TopbarView) findViewById(R.id.setting_tv);
        mTopBar.setMiddleText(getResources().getString(R.string.Main_Setting));
        mLeft = (ImageView) mTopBar.findViewById(R.id.left_btn);
        mLeft.setImageResource(R.drawable.photo_back_selector);

        mSettingUpdate = (RelativeLayout) findViewById(R.id.setting_update);
        mControlTB = (ToggleButton) findViewById(R.id.control_switch);
        mMessageTB = (ToggleButton) findViewById(R.id.message_switch);
        mMeTB = (ToggleButton) findViewById(R.id.me_switch);

//        Constants.mControlFlag = sp.getBoolean("controlFlag",false);
//        Constants.mMessageFlag = sp.getBoolean("messageFlag",false);
//        Constants.mMeFlag = sp.getBoolean("meFlag",false);

        mControlTB.setChecked(Constants.mControlFlag);
        mMessageTB.setChecked(Constants.mMessageFlag);
        mMeTB.setChecked(Constants.mMeFlag);

        mControlTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Constants.mControlFlag = b;
                //sp.edit().putBoolean("controlFlag",b);
                //sp.edit().commit();
            }
        });
        mMessageTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Constants.mMessageFlag = b;
                //sp.edit().putBoolean("messageFlag",b);
                //sp.edit().commit();
            }
        });
        mMeTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Constants.mMeFlag = b;
                //sp.edit().putBoolean("meFlag",b);
                //sp.edit().commit();
            }
        });

        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });

        mSettingUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this,"目前版本已是最新",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
