package com.shuai.futures.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.DiskLruCache.Editor;
import com.jakewharton.disklrucache.DiskLruCache.Snapshot;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.shuai.futures.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class HttpResponseCache {
	private static final String TAG=HttpResponseCache.class.getSimpleName();
	
	private static final String HTTP_RESPONSE_CACHE_DIR = "http_cache";
	private static DiskLruCache mDiskLruCache;
	private static HttpResponseCache mSelf;

	public static void init(Context appContext) {
		try {
			File cacheDir = StorageUtils.getCacheDirectory(appContext);

			File individualCacheDir = new File(cacheDir,
					HTTP_RESPONSE_CACHE_DIR);
			if (!individualCacheDir.exists()) {
				if (!individualCacheDir.mkdir()) {
					individualCacheDir = cacheDir;
				}
			}

			mDiskLruCache = DiskLruCache.open(individualCacheDir, 1, 1, 20 * 1024 * 1024);
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public static HttpResponseCache getInstance(){
		if (mSelf == null) {
			synchronized (HttpResponseCache.class) {
				if (mSelf == null) {
					mSelf = new HttpResponseCache();
				}
			}
		}
		return mSelf;
	}
	
	protected HttpResponseCache(){
		
	}
	
	private String getHashKey(String url){
		return Utils.md5(url).toLowerCase(Locale.getDefault());
	}
	
	public String get(String url){
		if(TextUtils.isEmpty(url)){
			return null;
		}
		
		String key=getHashKey(url);
		String result=null;
		try {
			Snapshot snapshot = mDiskLruCache.get(key);
			result = snapshot.getString(0);
			snapshot.close();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
		
		return result;
	}
	
	public void set(String url, String data){
		if(TextUtils.isEmpty(url)){
			return;
		}
		
		String key=getHashKey(url);
		try {
			Editor edit = mDiskLruCache.edit(key);
			edit.set(0, data);
			edit.commit();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public void flush(){
		try {
			mDiskLruCache.flush();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}
}
