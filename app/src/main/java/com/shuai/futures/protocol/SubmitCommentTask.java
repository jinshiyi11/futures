package com.shuai.futures.protocol;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.UserInfo;
import com.shuai.futures.logic.UserManager;

import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * 提交评论
 */
public class SubmitCommentTask extends BaseAutoReloginTask<Void> {
    private final static String TAG = SubmitCommentTask.class.getSimpleName();

    /**
     * @param context
     * @param futuresId
     * @param pid           被回复的评论的id，如果是直接发评论而不是回复别人，该参数为null
     * @param comment
     * @param imageUrl
     * @param listener
     * @param errorListener
     */
    public SubmitCommentTask(Context context, String futuresId, long pid, long ppid, String comment, String imageUrl, Listener<Void> listener, ErrorListener errorListener) {
        super(Method.POST, UrlHelper.getUrl(context, "api/addComment"),
                getBody(context, futuresId, pid, ppid, comment, imageUrl), listener, errorListener);
    }

    private static List<BasicNameValuePair> getBody(Context context, String futuresId, long pid, long ppid, String comment, String imageUrl) {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

        UserInfo accountInfo = UserManager.getInstance().getUserInfo();

        if (accountInfo != null) {
            params.add(new BasicNameValuePair("uid", String.valueOf(accountInfo.getUid())));
            params.add(new BasicNameValuePair("token", accountInfo.getToken()));
        }

        params.add(new BasicNameValuePair("futuresId", futuresId));
        params.add(new BasicNameValuePair("pid", Long.toString(pid)));
        params.add(new BasicNameValuePair("ppid", Long.toString(ppid)));
        params.add(new BasicNameValuePair("content", comment));

        if (!TextUtils.isEmpty(imageUrl))
            params.add(new BasicNameValuePair("comment_pic", imageUrl));

        UrlHelper.addCommonParameters(context, params);
        return params;
    }

    @Override
    protected Response<Void> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (Constants.DEBUG) {
                Log.d(TAG, jsonString);
            }

            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(jsonString).getAsJsonObject();
            ErrorInfo error = ProtocolUtils.getProtocolInfo(root);
            if (error.getErrorCode() != 0) {
                return Response.error(error);
            }

            return Response.success(null,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

}
