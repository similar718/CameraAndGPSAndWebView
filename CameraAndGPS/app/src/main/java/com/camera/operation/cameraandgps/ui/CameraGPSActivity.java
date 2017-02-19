package com.camera.operation.cameraandgps.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.camera.operation.cameraandgps.R;
import com.camera.operation.cameraandgps.util.BitmapUtils;
import com.camera.operation.cameraandgps.util.Constants;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
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

        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        mLocationClient.start();

        initLocation();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_UPDATE_LOCATION:
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("MMddHHmmss");
                    String date = sDateFormat.format(new Date());
                    mShowGPSTv.setText("当前经度：" + Constants.LongitudeStr + "\n" + "当前纬度：" + Constants.LatitudeStr+ "\n" + "当前时间：" + date);
                    break;
            }
        }
    };

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("MMddHHmmss");
            String str1 = ("").equals(Constants.LongitudeStr) || Constants.LongitudeStr == null || "4.9E-324".equals(Constants.LongitudeStr) ? "000D" : Constants.LongitudeStr.split("\\.")[0] + "D" + Constants.LongitudeStr.split("\\.")[1];
            String str2 = ("").equals(Constants.LatitudeStr) || Constants.LatitudeStr == null || "4.9E-324".equals(Constants.LatitudeStr) ? "000D" : Constants.LatitudeStr.split("\\.")[0] + "D" + Constants.LatitudeStr.split("\\.")[1];
            String date = sDateFormat.format(new Date());
            Constants.getPictureTime = date;
            String fileStr = Constants.SysFilePhotoPath + str1 + "-" + str2 + "-" + date + ".jpg";
            File tempFile = new File(fileStr);
            try {
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();
                Intent intent1 = new Intent(CameraGPSActivity.this, ShowCaptureActivity.class);
                intent1.putExtra("path", fileStr);
                startActivity(intent1);
                finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

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
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

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
                mShowGPSTv.setText("当前经度：" + Constants.LongitudeStr + "\n" + "当前纬度：" + Constants.LatitudeStr);
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
        //parameters.setPictureSize(Constants.WIDTH, Constants.HEIGHT);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//自动对焦
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

        mLocationClient.stop();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Constants.LongitudeStr = location.getLongitude()+"";
            Constants.LatitudeStr = location.getLatitude()+"";

            mHandler.sendEmptyMessage(SHOW_UPDATE_LOCATION);
            //if (mShowGPSTv != null)
                //mShowGPSTv.setText("当前经度：" + Constants.LongitudeStr + " " + "当前纬度：" + Constants.LatitudeStr);
            //Log.i("BaiduLocationApiDem", "当前经度：" + Constants.LongitudeStr + " " + "当前纬度：" + Constants.LatitudeStr);
            Log.i("BaiduLocationApiDem", sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
}
