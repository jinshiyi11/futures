package com.shuai.futures.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shuai.futures.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static void showShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 在浏览器中打开url
     * 
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * md5加密
     * @param s
     * @return
     */
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
    
    public static boolean checkPhoneNumber(String phone) {
		if (TextUtils.isEmpty(phone) || phone.length() != 11) {
			return false;
		} else
			return true;
	}
    
    public static void updateDialogWidth(Context context, Dialog dlg){
		int width = (int) DisplayUtils.getScreenWidth(context);
		dlg.getWindow().setLayout((int) (width*(6.0/7)), WindowManager.LayoutParams.WRAP_CONTENT);
    }
    
    /**
     * 因为String.valueOf(null)返回"null",对该情况特殊处理
     * @param str
     * @return
     */
    public static String getNonNullString(String str){
    	if(str==null)
    		return "";
    	
    	return str;
    }
    
    /**
     * 处理用户输入的用户名，太长就自动截断
     * @param userName
     * @return
     */
    public static String processUserName(String userName){
    	if(!TextUtils.isEmpty(userName) && userName.length()>8){
    		return userName.substring(0, 8);
    	}
    	
    	return userName;
    }
    
    /**
     * float类型的字符串表示.如果小数点后全为零，返回一个整数
     * @param number
     * @return
     */
    public static String getFloatString(float number){
    	/*
    	 *  float number=17.8;
			String result=String.format("%f", number);
			result为17.799999
    	 */
    	String result= String.valueOf(number);//String.format("%f", number);
    	int index=result.indexOf('.');
    	if(index!=-1){
    		for(int i=result.length()-1;i>index;i--){
    			if(result.charAt(i)!='0'){
    				return result.substring(0,i+1);
    			}
    		}
    		
    		return result.substring(0,index);
    	}
    	
    	return result;
    }
    
    /**
     * 获取yyyy-MM-dd对应的毫秒数
     * @param text
     * @return
     */
	public static long getDateTimeValue(String text) {
		long result = 0;

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			result = format.parse(text).getTime();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

		return result;
	}
	
	public static String getDateTimeText(long date) {
		String result = "";

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			result = format.format(new Date(date));
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

		return result;
	}
    
    public static java.util.Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
    
    public static void shakeView(View view){
    	YoYo.with(Techniques.Shake).duration(1000).playOn(view);
    }
    
    /**
     * 帮助用户生成一个随机密码
     * @return
     */
    public static String generateRandomPassword(){
    	StringBuilder builder=new StringBuilder();
    	Random random=new Random();
    	for(int i=0;i<8;i++){
    		builder.append((char)('a'+random.nextInt(26)));
    	}
    	
    	return builder.toString();
    }
    
    /**
     * 
     * @param context
     * @return
     */
    public static boolean isScreenLocked(Context context){
    	KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    	if( myKM.inKeyguardRestrictedInputMode()) {
    		//it is locked
    		return true;
    	} else {
    		//it is not locked
    		return false;
    	}
    }
    
    /**
     * 屏幕是否处于打开状态
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
	public static boolean isScreenOn(Context context) {

        if (android.os.Build.VERSION.SDK_INT >= 20) {

            // I'm counting                                                                                                                
            // STATE_DOZE, STATE_OFF, STATE_DOZE_SUSPENDED                                                                                 
            // all as "OFF"                                                                                                                

            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            for (Display display : dm.getDisplays ()) {
                if (display.getState () == Display.STATE_ON ||
                    display.getState () == Display.STATE_UNKNOWN) {
                    return true;
                }
            }

            return false;
        }

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager.isScreenOn ()) {
            return true;
        }
        return false;
    }
    
    /**
     * 让用户到应用市场给app评分
     * @param context
     */
	public static void showMarket(Context context) {
		try {
			Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, context.getString(R.string.launch_app_market_failed),
					Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 复制文档到剪切板
	 * @param context
	 * @param text
	 * @return
	 */
	@SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static boolean copyToClipboard(Context context, String text) {
        try {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                        .getSystemService(context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                        .getSystemService(context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData
                        .newPlainText("", text);
                clipboard.setPrimaryClip(clip);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	@SuppressLint("NewApi")
    public static String readFromClipboard(Context context) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                    .getSystemService(context.CLIPBOARD_SERVICE);
            return clipboard.getText().toString();
        } else {
        	android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);

            // Gets a content resolver instance
        	android.content.ContentResolver cr = context.getContentResolver();

            // Gets the clipboard data from the clipboard
        	android.content.ClipData clip = clipboard.getPrimaryClip();
            if (clip != null) {

                String text = null;
                String title = null;

                // Gets the first item from the clipboard data
                android.content.ClipData.Item item = clip.getItemAt(0);

                // Tries to get the item's contents as a URI pointing to a note
                Uri uri = item.getUri();

                // If the contents of the clipboard wasn't a reference to a
                // note, then
                // this converts whatever it is to text.
                if (text == null) {
                    text = coerceToText(context, item).toString();
                }

                return text;
            }
        }
        return "";
    }

    @SuppressLint("NewApi")
    public static CharSequence coerceToText(Context context, android.content.ClipData.Item item) {
        // If this Item has an explicit textual value, simply return that.
        CharSequence text = item.getText();
        if (text != null) {
            return text;
        }

        // If this Item has a URI value, try using that.
        Uri uri = item.getUri();
        if (uri != null) {

            // First see if the URI can be opened as a plain text stream
            // (of any sub-type). If so, this is the best textual
            // representation for it.
            FileInputStream stream = null;
            try {
                // Ask for a stream of the desired type.
                AssetFileDescriptor descr = context.getContentResolver()
                        .openTypedAssetFileDescriptor(uri, "text/*", null);
                stream = descr.createInputStream();
                InputStreamReader reader = new InputStreamReader(stream,
                        "UTF-8");

                // Got it... copy the stream into a local string and return it.
                StringBuilder builder = new StringBuilder(128);
                char[] buffer = new char[8192];
                int len;
                while ((len = reader.read(buffer)) > 0) {
                    builder.append(buffer, 0, len);
                }
                return builder.toString();

            } catch (FileNotFoundException e) {
                // Unable to open content URI as text... not really an
                // error, just something to ignore.

            } catch (IOException e) {
                // Something bad has happened.
                Log.w("ClippedData", "Failure loading text", e);
                return e.toString();

            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }

            // If we couldn't open the URI as a stream, then the URI itself
            // probably serves fairly well as a textual representation.
            return uri.toString();
        }

        // Finally, if all we have is an Intent, then we can just turn that
        // into text. Not the most user-friendly thing, but it's something.
        Intent intent = item.getIntent();
        if (intent != null) {
            return intent.toUri(Intent.URI_INTENT_SCHEME);
        }

        // Shouldn't get here, but just in case...
        return "";
    }

    public static String generateTmpFileBaseName(){
        long time= System.currentTimeMillis();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(time));
        return String.format("%s_%d",timeStamp,time);
    }

    /**
     * 生成一个文件名
     * @param fileExtension
     * @return
     */
    public static String generateTmpFileName(String fileExtension){
        if(!TextUtils.isEmpty(fileExtension))
            return generateTmpFileBaseName()+"."+fileExtension;
        else
            return generateTmpFileBaseName();
    }

    public static String getUrlFileExtension(String url){
        if(url==null)
            return null;

        int start=url.lastIndexOf('?');
        int i;
        if(start==-1)
            i = url.lastIndexOf('.');
        else
            i=url.lastIndexOf('.', start);
        if(i==-1)
            return null;

        //int seperator=url.lastIndexOf("")
        return start==-1?url.substring(i+1):url.substring(i+1,start);
    }

    public static String generateTmpFileNameFromUrl(String url){
        return generateTmpFileName(getUrlFileExtension(url));
    }


    /**
     * 将图片保存到手机上
     * @param context
     * @param imageUrl
     */
    public static void saveImage(Context context, String imageUrl){
        final WeakReference<Context> contextRef=new WeakReference<Context>(context);

        ImageLoader.getInstance().loadImage(imageUrl, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Context context=contextRef.get();
                if(context==null)
                    return;

                Toast.makeText(context, context.getString(R.string.save_pic_failed, failReason.getType().toString()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Context context=contextRef.get();
                if(context==null)
                    return;

                File imageFile = ImageLoader.getInstance().getDiskCache().get(imageUri);
                try {
                    File saveFile=new File(StorageUtils.getMyPicturesDirectory(),generateTmpFileNameFromUrl(imageUri));
                    StorageUtils.copyFile(imageFile, saveFile);

                    // Tell the media scanner about the new file so that it is
                    // immediately available to the user.
                    MediaScannerConnection.scanFile(context, new String[]{saveFile.toString()}, null, null);

                    Toast.makeText(context, context.getString(R.string.save_pic_success, saveFile.getAbsolutePath()), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(context, context.getString(R.string.save_pic_failed, ""), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
    }

}
