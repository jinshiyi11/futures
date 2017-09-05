package com.shuai.futures.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import java.io.IOException;

public class WallpaperUtils {

    /**
     * 设置壁纸
     * @param context
     * @param bitmap
     * @throws IOException
     */
    public static void setWallper(Context context, Bitmap bitmap) throws IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        DisplayMetrics displayMetrics = DisplayUtils.getDisplayMetrics(context);
        
        Rect screenRect=new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        
        int wallpaperWidth=screenRect.width();
        int wallpaperHeight=screenRect.height();
        //检查wallpaper的最小大小
        int desiredMinimumWidth = wallpaperManager.getDesiredMinimumWidth();
        int desiredMinimumHeight = wallpaperManager.getDesiredMinimumHeight();
        
        if(wallpaperWidth<desiredMinimumWidth){
            wallpaperWidth=desiredMinimumWidth;
        }
        if(wallpaperHeight<desiredMinimumHeight){
            wallpaperHeight=desiredMinimumHeight;
        }
        
        Bitmap wallpaperBitmap= Bitmap.createBitmap(wallpaperWidth, wallpaperHeight, Config.ARGB_8888);
        Canvas canvas=new Canvas(wallpaperBitmap);
        //图片使用center_crop缩放策略
        float scale;
        float dx = 0, dy = 0;
        Matrix matrix=new Matrix();
        int imagewidth=bitmap.getWidth();
        int imageheight=bitmap.getHeight();
        int screenWidth=screenRect.width();
        int screenheight=screenRect.height();

        if (imagewidth * screenheight > screenWidth * imageheight) {
            scale = (float) screenheight / (float) imageheight; 
            dx = (screenWidth - imagewidth * scale) * 0.5f;
        } else {
            scale = (float) screenWidth / (float) imagewidth;
            dy = (screenheight - imageheight * scale) * 0.5f;
        }

        matrix.setScale(scale, scale);
        matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        canvas.drawBitmap(bitmap, matrix, null);
        wallpaperManager.setBitmap(wallpaperBitmap);
        wallpaperBitmap.recycle();
    }

}
