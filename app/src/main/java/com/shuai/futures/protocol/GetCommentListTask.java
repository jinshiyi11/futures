package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.shuai.futures.data.Comment;
import com.shuai.futures.data.Constants;

import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 获取评论列表
 */
public class GetCommentListTask extends BaseTask<ArrayList<Comment>> {
    private final static String TAG = GetCommentListTask.class.getSimpleName();

    public GetCommentListTask(Context context, String futuresId, long startCommentId,int pageCount, boolean after, Listener<ArrayList<Comment>> listener, ErrorListener errorListener) {
        super(Method.GET, getUrl(context, futuresId, startCommentId,pageCount, after), null, listener, errorListener);
    }

    private static String getUrl(Context context, String futuresId, long startCommentId,int pageCount, boolean after) {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("futuresId", futuresId));
        params.add(new BasicNameValuePair("startCommentId", String.valueOf(startCommentId)));
        params.add(new BasicNameValuePair("pageCount", String.valueOf(pageCount)));
        params.add(new BasicNameValuePair("after", after ? "1" : "0"));

        return UrlHelper.getUrl(context, "api/getCommentList", params);
    }

    @Override
    protected Response<ArrayList<Comment>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (Constants.DEBUG) {
                Log.d(TAG, jsonString);
            }

            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(jsonString).getAsJsonObject();
            ErrorInfo error = ProtocolUtils.getProtocolInfo(root);
            if (error.getErrorCode() != 0) {
                return Response.error(error);
            }

            Gson gson = new Gson();
            JsonArray resultJson = root.getAsJsonArray(ProtocolUtils.DATA);
            Type type = new TypeToken<ArrayList<Comment>>() {
            }.getType();
            ArrayList<Comment> result = gson.fromJson(resultJson, type);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

}
