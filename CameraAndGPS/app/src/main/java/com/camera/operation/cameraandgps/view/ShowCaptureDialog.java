package com.camera.operation.cameraandgps.view;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/2/11.
 */

public class ShowCaptureDialog extends Dialog {

    private Context mContext = null;

    public ShowCaptureDialog(Context context){
        super(context);
        mContext = context;
    }
}
