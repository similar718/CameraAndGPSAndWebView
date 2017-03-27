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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.camera.operation.cameraandgps.R;
import com.camera.operation.cameraandgps.base.LocalEntity;
import com.camera.operation.cameraandgps.util.Constants;
import com.camera.operation.cameraandgps.util.DbHelper;
import com.camera.operation.cameraandgps.view.TopbarView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by similar on 2017/2/10.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mLeft;
    private TextView mRight;
    private RelativeLayout mSettingUpdate;
    private EditText mControlET;
    private EditText mMessageET;
    private EditText mMeET;
    private DbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView(){
        mHelper = new DbHelper(this);

        mLeft = (ImageView) findViewById(R.id.left_btn);
        mRight = (TextView) findViewById(R.id.right_text);
        mControlET = (EditText) findViewById(R.id.set_control_et);
        mMessageET = (EditText) findViewById(R.id.set_message_et);
        mMeET = (EditText) findViewById(R.id.set_me_et);
        mSettingUpdate = (RelativeLayout) findViewById(R.id.setting_update);

        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
        mSettingUpdate.setOnClickListener(this);

        mControlET.setText(Constants.mControlUrl);
        mMessageET.setText(Constants.mMessageUrl);
        mMeET.setText(Constants.mMeUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_btn:
                SettingActivity.this.finish();
                break;
            case R.id.right_text:
                if (!Constants.mControlUrl.equals(mControlET.getText().toString())){
                    putFileToSD();
                    Constants.mControlUrl = mControlET.getText().toString();
                }
                if (!Constants.mMessageUrl.equals(mMessageET.getText().toString())){
                    putFileToSD();
                    Constants.mMessageUrl = mMessageET.getText().toString();
                }
                if (!Constants.mMeUrl.equals(mMeET.getText().toString())){
                    putFileToSD();
                    Constants.mMeUrl = mMeET.getText().toString();
                }
                Toast.makeText(SettingActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_update:
                Toast.makeText(SettingActivity.this,"目前版本已是最新",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void putFileToSD()
    {
        LocalEntity entity = new LocalEntity();
        entity.mControlUrl = mControlET.getText().toString();
        entity.mMessageUrl = mMessageET.getText().toString();
        entity.mMeUrl = mMeET.getText().toString();
        mHelper.insert1(entity);

//        try
//        {
//            File file = new File(Constants.SysFilePhotoPathNeed + name + ".txt");
//            if (file.exists()){
//                file.delete();
//            }
//            FileOutputStream outStream = new FileOutputStream(Constants.SysFilePhotoPathNeed + name + ".txt",true);
//            OutputStreamWriter writer = new OutputStreamWriter(outStream,"UTF-8");
//            writer.write(message);
//            writer.flush();
//            writer.close();//记得关闭
//            outStream.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }
}
