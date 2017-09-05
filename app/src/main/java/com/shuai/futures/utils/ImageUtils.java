package com.shuai.futures.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtils {
	private static final String TAG = ImageUtils.class.getSimpleName();

	public static String getPhotoDir(Context context) {
		File filesDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		// File filesDir = context.getFilesDir();
		File photoDir = new File(filesDir, "photos");
		if (!photoDir.exists())
			photoDir.mkdirs();

		// photoDir.setWritable(true, false)
		return photoDir.getAbsolutePath();
	}

	public static void takePhoto(Activity context, Fragment fragment,
                                 String savePath, int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			File photoFile = new File(savePath);
			photoFile.setWritable(true, false);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

			if (fragment != null)
				fragment.startActivityForResult(intent, requestCode);
			else
				context.startActivityForResult(intent, requestCode);
		}
	}

	public static void compressPhoto(String imgPath, String newImgPath) {
		try {
			InputStream in = new FileInputStream(imgPath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;

			OutputStream out = new FileOutputStream(newImgPath);
			Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void setExifAttribute(ExifInterface sourceExif,
                                         ExifInterface destExif, String name) {
		String value = sourceExif.getAttribute(name);
		if (!TextUtils.isEmpty(value)) {
			destExif.setAttribute(name, value);
		}
	}

	public static void copyExifAttributes(String sourceImg, String destImg) {
		try {
			ExifInterface sourceExif = new ExifInterface(sourceImg);
			ExifInterface destExif = new ExifInterface(destImg);

			setExifAttribute(sourceExif, destExif, ExifInterface.TAG_DATETIME);
			setExifAttribute(sourceExif, destExif, ExifInterface.TAG_FLASH);
			setExifAttribute(sourceExif, destExif,
					ExifInterface.TAG_GPS_LATITUDE);
			setExifAttribute(sourceExif, destExif,
					ExifInterface.TAG_GPS_LATITUDE_REF);
			setExifAttribute(sourceExif, destExif,
					ExifInterface.TAG_GPS_LONGITUDE);
			setExifAttribute(sourceExif, destExif,
					ExifInterface.TAG_GPS_LONGITUDE_REF);
			setExifAttribute(sourceExif, destExif, ExifInterface.TAG_MAKE);
			setExifAttribute(sourceExif, destExif, ExifInterface.TAG_MODEL);
			setExifAttribute(sourceExif, destExif,
					ExifInterface.TAG_ORIENTATION);

			destExif.setAttribute("UserComment", "taiyanggong");// custom info
			destExif.saveAttributes();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

	}

	/**
	 * 保存图片为PNG
	 * 
	 * @param bitmap
	 * @param filePath
	 * @throws IOException
	 */
	public static void savePNG(Bitmap bitmap, String filePath) throws IOException {
		File file = new File(filePath);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
			}
		} finally {

			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 保存图片为JPEG
	 * 
	 * @param bitmap
	 * @param filePath
	 * @throws IOException
	 * @throws Exception
	 */
	public static void saveJPEG(Bitmap bitmap, String filePath) throws IOException {
		File file = new File(filePath);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
			}
		} finally {

			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
