package com.shuai.futures;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.shuai.futures.data.Constants;


/**
 * 短信监听
 *
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG=SMSBroadcastReceiver.class.getSimpleName();
	
	private MessageListener mMessageListener;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	
	public SMSBroadcastReceiver() {
        super();
    }

	@Override
	public void onReceive(Context context, Intent intent) {
	        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
                if(intent.getExtras()==null || intent.getExtras().get("pdus")==null){
                    return;
                }

                try {
                    Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        //短信内容
                        String content = smsMessage.getDisplayMessageBody();
                        long date = smsMessage.getTimestampMillis();

                        mMessageListener.onMessage(sender, content, date);
                        //abortBroadcast();
                    }
                }catch (Exception e){
                    if(Constants.DEBUG)
                        Log.e(TAG,e.toString());
                }
	        }
	}
	
	//回调接口
    public interface MessageListener  {
        public void onMessage(String sender, String message, long time);
    }
    
    public void setOnReceivedMessageListener(MessageListener messageListener) {
    	mMessageListener = messageListener;;
    }
}
