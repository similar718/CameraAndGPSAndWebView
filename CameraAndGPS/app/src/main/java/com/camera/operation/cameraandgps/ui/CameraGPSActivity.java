package com.camera.operation.cameraandgps.ui;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.camera.operation.cameraandgps.R;
import com.camera.operation.cameraandgps.util.Constants;
import com.camera.operation.cameraandgps.util.GPSUtil;

/**
 * Created by similar on 2017/2/9.
 */

public class CameraGPSActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    public Camera m_camera;
    SurfaceView m_prevewview;
    SurfaceHolder m_surfaceHolder;

    private Button mRequestGPSBtn;
    private Button mGetBtn;
    private Button mSaveBtn;
    private Button mBackBtn;
    private Button mLongitudeBtn;
    private Button mLatitudeBtn;
    private TextView mShowGPSTv;

    private GPSUtil mGpsUtil = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameragps);

        initView();
    }

    private void initView() {
        m_prevewview = (SurfaceView) findViewById(R.id.surface_view);
        m_surfaceHolder = m_prevewview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
        m_surfaceHolder.setFixedSize(Constants.WIDTH, Constants.HEIGHT); // 预览大小設置
        m_surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        m_surfaceHolder.addCallback(this);

        mRequestGPSBtn = (Button) findViewById(R.id.camera_requestGPS_btn);
        mGetBtn = (Button) findViewById(R.id.camera_get_btn);
        mSaveBtn = (Button) findViewById(R.id.camera_save_btn);
        mBackBtn = (Button) findViewById(R.id.camera_back_btn);
        mLongitudeBtn = (Button) findViewById(R.id.camera_longitude_btn);
        mLatitudeBtn = (Button) findViewById(R.id.camera_latitude_btn);
        mShowGPSTv = (TextView) findViewById(R.id.showGPS_tv);

        mRequestGPSBtn.setOnClickListener(this);
        mGetBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mLongitudeBtn.setOnClickListener(this);
        mLatitudeBtn.setOnClickListener(this);

        mGpsUtil = new GPSUtil(CameraGPSActivity.this);
    }

    private void initCameara() {
        try {
            m_camera = Camera.open();// 0 表示后摄像头  1表示前摄像头  默认为后摄像头
            m_camera.setPreviewDisplay(m_surfaceHolder);
            Camera.Parameters parameters = m_camera.getParameters();
            //设置预览尺寸
            parameters.setPreviewSize(Constants.WIDTH, Constants.HEIGHT);
            parameters.setPreviewFormat(ImageFormat.NV21);
            m_camera.setDisplayOrientation(0);
            m_camera.setParameters(parameters);
            m_camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void destroyCamera() {
        if (m_camera == null) {
            return;
        }
        m_camera.stopPreview();
        m_camera.release();
        m_camera = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) { }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        initCameara();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        destroyCamera();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_requestGPS_btn://获取GPS
                updateView(mGpsUtil.getGpsAddress());
                break;
            case R.id.camera_get_btn://拍照
                break;
            case R.id.camera_save_btn://保存图片
                break;
            case R.id.camera_back_btn://返回首页
                this.finish();
                break;
            case R.id.camera_longitude_btn://经度
                updateView("经度："+Constants.LongitudeStr);
                break;
            case R.id.camera_latitude_btn://纬度
                updateView("纬度："+Constants.LatitudeStr);
                break;
            default:
                break;
        }
    }

    /**
     * 实时更新文本内容
     */
    private void updateView(String message){
        if(message!=null){
            mShowGPSTv.setText(message);
        }else{
            mShowGPSTv.setText("经度：0 纬度：0");
        }
    }

}
