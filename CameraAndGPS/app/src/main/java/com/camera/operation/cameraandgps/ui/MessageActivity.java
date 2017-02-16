package com.camera.operation.cameraandgps.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.camera.operation.cameraandgps.R;
import com.camera.operation.cameraandgps.util.Constants;
import com.camera.operation.cameraandgps.view.TopbarView;

/**
 * Created by similar on 2017/2/10.
 */

public class MessageActivity extends AppCompatActivity {
    private WebView mWebView;
    private TopbarView mTopBar;
    private ImageView mLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
    }

    private void initView(){
        mWebView = (WebView) findViewById(R.id.message_wv);
        mTopBar = (TopbarView) findViewById(R.id.message_tv);
        mTopBar.setMiddleText(getResources().getString(R.string.Main_Message));
        mLeft = (ImageView) mTopBar.findViewById(R.id.left_btn);
        mLeft.setImageResource(R.drawable.photo_back_selector);

        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageActivity.this.finish();
            }
        });

        mWebView.loadUrl(Constants.mMessageUrl);
    }
}
