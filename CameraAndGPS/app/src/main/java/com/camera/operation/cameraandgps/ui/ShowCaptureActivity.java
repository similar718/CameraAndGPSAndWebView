package com.camera.operation.cameraandgps.ui;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.camera.operation.cameraandgps.R;
import com.camera.operation.cameraandgps.util.BitmapUtils;
import com.camera.operation.cameraandgps.view.ZoomImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import static android.provider.MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;

/**
 * Created by similar on 2017/2/12.
 */

public class ShowCaptureActivity extends AppCompatActivity implements View.OnClickListener {

    //private ZoomImageView mShowIv;
    private RelativeLayout mCancel;
    private ImageView mBack;
    private RelativeLayout mSave;

    private ImageView mShow;

    private String path = null;
    private Bitmap bmp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcapture_activity);

        initView();
    }
    private void initView(){
        //mShowIv = (ZoomImageView) findViewById(R.id.show_showCapture_iv);
        mCancel = (RelativeLayout) findViewById(R.id.show_cancel_rl);
        mBack = (ImageView) findViewById(R.id.show_back_btn);
        mSave = (RelativeLayout) findViewById(R.id.show_save_rl);
        mShow = (ImageView) findViewById(R.id.show_iv);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        path = bundle.getString("path");//getString()返回指定key的值
        Bitmap bmpDefaultPic = BitmapFactory.decodeFile(path,null);
        bmp = BitmapUtils.addStrToBitmap(bmpDefaultPic);
        //mShowIv.setImage(bmpDefaultPic);

        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mShow.setImageBitmap(bmp);
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
                BitmapUtils.saveMyBitmap(path,bmp);
                galleryAddPic();
                startUI();
                break;
            default:
                break;
        }
    }


    private void galleryAddPic(){
        File file = new File(path);
        Uri photoUri = Uri.fromFile(file);
        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(photoUri);
        this.sendBroadcast(mediaScanIntent);
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
        ShowCaptureActivity.this.finish();
    }
}
