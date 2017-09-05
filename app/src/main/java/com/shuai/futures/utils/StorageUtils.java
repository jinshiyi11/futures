package com.shuai.futures.utils;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.io.input.BOMInputStream;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;

public class StorageUtils {
    /**
     * 目录或文件是否存在
     * @param filePath
     * @return
     */
    public static boolean fileExists(String filePath){
        File file=new File(filePath);
        return file.exists();
    }
    
    /**
     * 将assets下的资源文件读入字符串
     * @param context
     * @param assetFilePath
     * @return
     * @throws IOException
     */
    public static String getAssetUTF8FileData(Context context, String assetFilePath) throws IOException {
        String result=getAssetFileData(context,assetFilePath,"UTF-8");
        return result;
    }
    
    public static String getAssetFileData(Context context, String assetFilePath, final String charsetName) throws IOException {
        InputStream stream = context.getAssets().open(assetFilePath);
        
        BOMInputStream bomIn = new BOMInputStream(stream);
        String result=inputStreamToString(bomIn);
        stream.close();
        return result;
    }
    
    public static String getUTF8FileData(Context context, String filePath) throws IOException {
        String result=getFileData(context,filePath,"UTF-8");
        return result;
    }
    
    public static String getFileData(Context context, String filePath, final String charsetName) throws IOException {
        InputStream stream = new FileInputStream(filePath);
        InputStreamReader in=new InputStreamReader(stream, charsetName);
        String result=inputStreamToString(in);
        stream.close();
        return result;
    }
    
    public static String inputStreamToString(Reader in){
        StringBuilder sb=new StringBuilder();
        
        BufferedReader br = new BufferedReader(in);
        try {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    public static String inputStreamToString(InputStream input){
        String result=null;
        
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        int bytesRead;
        try {
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
        result=output.toString();
        return result;
    }
    
    /**
     * 复制文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFile(File source, File dest)
            throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
    
    public static File getExternalPicturesDirectory(){
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        
        return path;
    }
    
    public static File getExternalDcimDirectory(){
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        
        return path;
    }
    
    public static File getMyPicturesDirectory(){
        File path = getExternalPicturesDirectory();
        
        File file = new File(path, "baoba");
        if(!file.exists())
            file.mkdirs();
        
        return file;
    }


}
