package com.shuai.futures.utils;

import android.util.Log;

import java.io.File;

public class FileUtils {
	private static final String TAG=FileUtils.class.getSimpleName();

    /**
     * 获取文件的扩展名
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        if(fileName==null)
            return "";

        if (fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
    
    /**
     * 获取文件的名称，不包含扩展名
     * @param fileName
     * @return
     */
    public static String getFileBasename(String fileName) {
        if(fileName==null)
            return "";

        if (fileName.lastIndexOf(".") != -1) {
            return fileName.substring(0,fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }
    
    public static long getFileSize(String filePath){
    	try{
    		File file = new File(filePath);
    		return file.length();
    	}catch(Exception e){
    		Log.e(TAG, e.toString());
    	}
    	
    	return 0;
    }

}
