package com.camera.operation.cameraandgps.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.camera.operation.cameraandgps.R;
import com.camera.operation.cameraandgps.view.ZoomImageView;

import java.io.File;

/**
 * Created by similar on 2017/2/12.
 */

public class ShowCaptureActivity extends AppCompatActivity implements View.OnClickListener {

    private ZoomImageView mShowIv;
    private RelativeLayout mCancel;
    private ImageView mBack;
    private RelativeLayout mSave;

    private String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcapture_activity);

        initView();
    }
    private void initView(){
        mShowIv = (ZoomImageView) findViewById(R.id.show_showCapture_iv);
        mCancel = (RelativeLayout) findViewById(R.id.show_cancel_rl);
        mBack = (ImageView) findViewById(R.id.show_back_btn);
        mSave = (RelativeLayout) findViewById(R.id.show_save_rl);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        path = bundle.getString("path");//getString()返回指定key的值
        Bitmap bmpDefaultPic = BitmapFactory.decodeFile(path,null);
        //mShowIv.setBitmap(bmpDefaultPic);
        mShowIv.setImage(bmpDefaultPic);

        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.show_back_btn:
                cancelSave();
                break;
            case  R.id.show_cancel_rl:
               cancelSave();
                break;
            case R.id.show_save_rl:
                startUI();
                break;
            default:
                break;
        }
    }

    private void cancelSave(){
        File file = new File(path);
        if (file != null || file.exists() || !file.isDirectory()){
            file.delete();
        }
        startUI();
    }
    private void startUI(){
        Intent intent = new Intent(ShowCaptureActivity.this,CameraGPSActivity.class);
        startActivity(intent);
    }
}
