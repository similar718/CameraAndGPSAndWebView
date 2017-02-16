package com.camera.operation.cameraandgps.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.camera.operation.cameraandgps.R;

public class TopbarView extends LinearLayout  {
	
	private ImageView mLeftBtn;
	private Context mContext;
	private ImageView mRightBtn;
	private TextView mTitleTextView;

	public TopbarView(Context context, AttributeSet attrs) {
		super(context, attrs);		
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.view_topbar, this, true);
		mLeftBtn = (ImageView)findViewById(R.id.left_btn);
		mTitleTextView = (TextView)findViewById(R.id.top_text);
		mRightBtn = (ImageView)findViewById(R.id.right_btn);
	}

	public TopbarView(Context context, ImageView mLeftBtn, Context mContext) {
		super(context);
		this.mLeftBtn = mLeftBtn;
		this.mContext = mContext;
	}

	public void setLeftBtnAttr(int resId , OnClickListener clickable){
		mLeftBtn.setImageResource(resId);
		mLeftBtn.setOnClickListener(clickable);
	}
	
	public void setMiddleText(String title){
		mTitleTextView.setText(title);
	}
	
	public void setRightBtnAttr(int resId, OnClickListener clickable){
		mRightBtn.setImageResource(resId);
		mRightBtn.setOnClickListener(clickable);
	}
	
}
