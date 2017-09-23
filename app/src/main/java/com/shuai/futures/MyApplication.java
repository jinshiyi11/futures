package com.shuai.futures;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shuai.futures.data.Config;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.data.HttpResponseCache;
import com.shuai.futures.logic.UserManager;
import com.shuai.futures.net.ConnectionChangeMonitor;
import com.shuai.futures.net.CustomImageDownloader;
import com.shuai.futures.net.MyHttpStack;
import com.shuai.futures.utils.AppUtils;
import com.shuai.futures.utils.ProcessUtils;
import com.shuai.futures.utils.StorageUtils;
import com.shuai.futures.view.chart.XLabelInfo;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static android.R.attr.type;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    private static Context mContext;
    private static Map<String, XLabelInfo> mXLabelMap = new HashMap<>();


    /**
     * 网络异步请求对象,用于协议数据的获取
     */
    private static RequestQueue mRequestQueue;
    private ConnectionChangeMonitor mConnectionChangeMonitor;

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null)
            return mRequestQueue;
        else {
            //不应该为null
            throw new NullPointerException("mRequestQueue is null!");
        }
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static Map<String, XLabelInfo> getXLabelMap() {
        return mXLabelMap;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        int myPid = Process.myPid();
        Log.d("myPid", String.valueOf(myPid));
        String packageName = AppUtils.getApplicationInfo(mContext).packageName;
        String processName = ProcessUtils.getProcessNameByPid(mContext, myPid);

        //不是service子进程才执行初始化，防止多次初始化
        if (processName != null && processName.equals(packageName)) {
            init();
        }

    }

    private void init() {
//		if(Constants.DEBUG){
//		    com.umeng.socialize.utils.Log.LOG = true;
//			MobclickAgent.setDebugMode(true);
//			UpdateConfig.setDebug(true);
//		}

        // 初始化网络异步请求对象
        if (Constants.DEBUG) {
            mRequestQueue = Volley.newRequestQueue(this, new MyHttpStack());
        } else
            mRequestQueue = Volley.newRequestQueue(this);

        Config.getInstance().init(this);
        UserManager userManager = UserManager.getInstance();
        userManager.init(this);
        initImageLoader();
        HttpResponseCache.init(this);

        Config.getInstance().loadConfig();
        mConnectionChangeMonitor = new ConnectionChangeMonitor(this);
        mConnectionChangeMonitor.startMonitor();

        //因为包含fragment，禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        loadXLabels();

//        EventBus.getDefault().register(this);

//        if(!userManager.isLogined())
//            userManager.autoLogin();

//        Set<String> tags=new HashSet<String>();
//        tags.add(AppUtils.getChannel(mContext));
//        JPushInterface.setTags(mContext,tags,new TagAliasCallback(){
//
//            @Override
//            public void gotResult(int responseCode, String alias, Set<String> tags) {
//                if(Constants.DEBUG){
//                    Log.d(TAG,String.valueOf(responseCode));
//                }
//
//            }
//        });
    }

    private void initImageLoader() {
//        Map<String, String> headers =new HashMap<String, String>();
//        headers.put(Constants.HTTP_REFERER_KEY,Constants.HTTP_REFERER_VALUE);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .extraForDownloader(headers)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .imageDownloader(new CustomImageDownloader(mContext))
                .defaultDisplayImageOptions(defaultOptions)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(3 * 1024 * 1024)
                .denyCacheImageMultipleSizesInMemory()
                //.discCacheSize(10 * 1024 * 1024)
                .diskCacheFileCount(150)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        EventBus.getDefault().unregister(this);
    }

    private void loadXLabels() {
        try {
            String fileData = StorageUtils.getAssetUTF8FileData(this, "document.json");
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(fileData).getAsJsonArray();
            for (int i = 0; i < array.size(); ++i) {
                JsonObject element = (JsonObject) array.get(i);
                String[] keys = element.get("key").getAsString().toLowerCase().split(",");
                XLabelInfo item = gson.fromJson(element.get("data"), XLabelInfo.class);
                for (String key : keys) {
                    if (mXLabelMap.get(key) != null) {
                        Log.e(TAG, "duplicate key!");
                    }
                    mXLabelMap.put(key, item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
