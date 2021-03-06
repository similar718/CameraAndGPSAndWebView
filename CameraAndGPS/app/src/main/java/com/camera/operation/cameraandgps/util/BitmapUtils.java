package com.camera.operation.cameraandgps.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class BitmapUtils {

    /**
     * add text to image
     */
	public static Bitmap addStrToBitmap(Bitmap src){
		Bitmap newb= Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);
        canvas.drawBitmap(src, 0, 0, new Paint());
        Paint paint = new Paint();
        paint.setTextSize(48);
        String familyName = "宋体";
        Typeface font = Typeface.create(familyName,Typeface.NORMAL);
        paint.setTypeface(font);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);


        String timeStr = "经度：" + Constants.LongitudeStr + "\n 纬度：" + Constants.LatitudeStr+"\n 时间："+Constants.getPictureTime;
        int strWidth = getStringWidth(paint, timeStr);
        int strHeight = getStringHeight(paint, timeStr);
        int startX = newb.getWidth() - strWidth;
        int startY = newb.getHeight() - strHeight + 36;
        canvas.drawText(timeStr, startX, startY, paint);
        return newb;
	}

    /**
     * Save picture with latitude and langitude
     * @param bitName filepath
     * @param mBitmap file bitmap
     */
    public static void saveMyBitmap(String bitName,Bitmap mBitmap){
        File f = new File(bitName);
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getStringWidth(Paint paint, String str) {
		return (int) paint.measureText(str);
	}

	private static int getStringHeight(Paint paint, String str) {
		FontMetrics fr = paint.getFontMetrics();
		return (int) Math.ceil(fr.descent -fr.top);
	}
}
