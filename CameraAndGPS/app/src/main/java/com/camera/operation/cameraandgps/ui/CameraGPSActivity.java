package com.camera.operation.cameraandgps.ui;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.camera.operation.cameraandgps.R;
import com.camera.operation.cameraandgps.util.Constants;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
    private ImageView mBackIv;

    private static final int SHOW_UPDATE_LOCATION = 5001;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameragps);

        initView();
        isFileExits();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void isFileExits() {
        File destDir = new File(Constants.SysFilePhotoPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void startLocation(){
        //根据控件的选择，重新设置定位参数
        //resetOption();
        getDefaultOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private static boolean mIsFlag = true;
    private static int flag = 3;


    public class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (mIsFlag){
                if (flag>2) {
                    startLocation();
                    flag--;
                } else {
                    flag--;
                    if (flag == 1){
                        stopLocation();
                    }
                    if (flag == 0){
                        flag = 3;
                    }
                }
                startLocation();
                mHandler.sendEmptyMessage(SHOW_UPDATE_LOCATION);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                //String result = Utils.getLocationStr(loc);
                //tvResult.setText(result);
                Constants.LongitudeStr = loc.getLongitude()+"";
                Constants.LatitudeStr = loc.getLatitude()+"";
            } else {
                //tvResult.setText("定位失败，loc is null");
            }
            mHandler.sendEmptyMessage(SHOW_UPDATE_LOCATION);
        }
    };

    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    private void initView() {
        m_prevewview = (SurfaceView) findViewById(R.id.surface_view);
        m_surfaceHolder = m_prevewview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
        m_surfaceHolder.setFixedSize(Constants.WIDTH, Constants.HEIGHT); // 预览大小設置
        m_surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        m_surfaceHolder.addCallback(this);
        //点击自动对焦
        m_prevewview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_camera.autoFocus(null);
            }
        });

        mRequestGPSBtn = (Button) findViewById(R.id.camera_requestGPS_btn);
        mGetBtn = (Button) findViewById(R.id.camera_get_btn);
        //mGetBtn = (Button) findViewById(R.id.picture_bt);
        mSaveBtn = (Button) findViewById(R.id.camera_save_btn);
        mBackBtn = (Button) findViewById(R.id.camera_back_btn);
        mLongitudeBtn = (Button) findViewById(R.id.camera_longitude_btn);
        mLatitudeBtn = (Button) findViewById(R.id.camera_latitude_btn);
        mShowGPSTv = (TextView) findViewById(R.id.showGPS_tv);
        mBackIv = (ImageView) findViewById(R.id.camera_back_iv);

        mRequestGPSBtn.setOnClickListener(this);
        mGetBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mLongitudeBtn.setOnClickListener(this);
        mLatitudeBtn.setOnClickListener(this);
        mBackIv.setOnClickListener(this);

        String familyName = "宋体";
        Typeface font = Typeface.create(familyName,Typeface.NORMAL);
        mShowGPSTv.setTypeface(font);

        //locationClient = new AMapLocationClient(this.getApplicationContext());
        initLocation();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_UPDATE_LOCATION:
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sDateFormat.format(new Date());
                    mShowGPSTv.setText("经度：" + Constants.LongitudeStr + "\n" + "纬度：" + Constants.LatitudeStr+ "\n" + "时间：" + date);
                    break;
            }
        }
    };

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String str1 = ("").equals(Constants.LongitudeStr) || Constants.LongitudeStr == null || "4.9E-324".equals(Constants.LongitudeStr) ? "000D" : Constants.LongitudeStr.split("\\.")[0] + "D" + Constants.LongitudeStr.split("\\.")[1];
            String str2 = ("").equals(Constants.LatitudeStr) || Constants.LatitudeStr == null || "4.9E-324".equals(Constants.LatitudeStr) ? "000D" : Constants.LatitudeStr.split("\\.")[0] + "D" + Constants.LatitudeStr.split("\\.")[1];
            String date = sDateFormat.format(new Date());
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Constants.getPictureTime = dates.format(new Date());
            String fileStr = Constants.SysFilePhotoPath + str1 + "-" + str2 + "-" + date + ".jpg";
            File tempFile = new File(fileStr);
            try {
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();
                Intent intent1 = new Intent(CameraGPSActivity.this, ShowCaptureActivity.class);
                intent1.putExtra("path", fileStr);
                //intent1.putExtra("data",data);
                startActivity(intent1);
                CameraGPSActivity.this.finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void initCamera() {
        requestCameraPictureSize();
        try {
            m_camera = Camera.open();// 0 表示后摄像头  1表示前摄像头  默认为后摄像头
            m_camera.setPreviewDisplay(m_surfaceHolder);
            Camera.Parameters parameters = m_camera.getParameters();
            //设置预览尺寸
            parameters.setPreviewSize(Constants.WIDTH, Constants.HEIGHT);
            parameters.setPictureSize(Constants.PWIDTH,Constants.PHEIGHT);
            parameters.setPreviewFormat(ImageFormat.NV21);
            m_camera.setDisplayOrientation(0);
            m_camera.setParameters(parameters);
            m_camera.startPreview();

            mIsFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int width = 0;
    private int height = 0;
    private void requestCameraPictureSize(){
        Camera camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> listpicture = parameters.getSupportedPictureSizes();
        if (listpicture.size() > 1) {
            Iterator<Camera.Size> itor = listpicture.iterator();
            Camera.Size cur = null;
            while (itor.hasNext()) {
                cur = itor.next();
                //if (cur.width<3000) {
                    if (cur.width > width || cur.height > height) {
                        width = cur.width;
                        height = cur.height;
                    }
                //}
            }
        }
        camera.release();
        camera = null;
        Constants.PWIDTH = width;
        Constants.PHEIGHT = height;
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
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        destroyCamera();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_requestGPS_btn://获取GPS
                mShowGPSTv.setText("经度：" + Constants.LongitudeStr + "\n" + "纬度：" + Constants.LatitudeStr);
                break;
            case R.id.camera_get_btn://拍照
            //case R.id.picture_bt:
                capture(mGetBtn);
                break;
            case R.id.camera_save_btn://保存图片
                break;
            case R.id.camera_back_iv://返回首页
                this.finish();
                break;
            case R.id.camera_longitude_btn://经度
                mShowGPSTv.setText("经度：" + Constants.LongitudeStr);
                break;
            case R.id.camera_latitude_btn://纬度
                mShowGPSTv.setText("纬度：" + Constants.LatitudeStr);
                break;
            default:
                break;
        }
    }

    /**
     * 拍照
     *
     * @param view
     */
    public void capture(View view) {
        Camera.Parameters parameters = m_camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//自动对焦
        m_camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                //对焦准确拍摄照片
                if (success) {
                    m_camera.takePicture(null, null, mPictureCallback);
                }
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CameraGPS Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    Thread thread = new MyThread();
    @Override
    public void onStart() {
        super.onStart();
        //thread.start();
        //startLocation();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsFlag = true;
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();
        mIsFlag = false;
    }

    @Override
    public void onStop() {
        super.onStop();
//        stopLocation();
//        mIsFlag = false;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
}
