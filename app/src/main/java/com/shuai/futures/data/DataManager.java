package com.shuai.futures.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.shuai.futures.event.FollowedFuturesAddedEvent;
import com.shuai.futures.event.FollowedFuturesRefreshedEvent;
import com.shuai.futures.utils.IoUtils;
import com.shuai.futures.utils.LogUtils;

import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class DataManager {
    private static final String TAG = DataManager.class.getSimpleName();
    private static DataManager mSelf;
    private static Context mAppContext;
    private List<FuturesInfo> mFollowedFutures = new LinkedList<>();
    private DbHelper mDbHelper;

    public static void init(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public static DataManager getInstance() {
        if (mSelf == null) {
            synchronized (DataManager.class) {
                if (mSelf == null) {
                    mSelf = new DataManager();
                }
            }
        }
        return mSelf;
    }

    private DataManager() {
        mDbHelper = new DbHelper(mAppContext);
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = mDbHelper.getReadableDatabase();
            cursor = database.rawQuery("SELECT futures_id,futures_name,futures_title FROM followed ORDER BY insert_time DESC", null);
            while (cursor.moveToNext()) {
                FuturesInfo item = new FuturesInfo();
                item.mId = cursor.getString(0);
                item.mName = cursor.getString(1);
                item.mTitle = cursor.getString(2);
                mFollowedFutures.add(item);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "", e);
        } finally {
            IoUtils.closeQuietly(cursor);
        }

    }

    public List<FuturesInfo> getFollowedFutures() {
        return mFollowedFutures;
    }

    public boolean isFollowedFutures(String futuresId) {
        boolean found = false;
        for (FuturesInfo item : mFollowedFutures) {
            if (item.mId.equals(futuresId)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public void refreshFollowedFutures(List<FuturesInfo> futuresInfoList) {
        mFollowedFutures.clear();
        if (futuresInfoList != null) {
            mFollowedFutures.addAll(futuresInfoList);
            try {
                SQLiteDatabase database = mDbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    database.delete(DbHelper.FOLLOWED_TABLE, null, null);
                    SQLiteStatement statement = database.compileStatement("INSERT INTO followed" +
                            "(futures_id,futures_name,futures_title) values(?,?,?)");
                    for (FuturesInfo item : futuresInfoList) {
                        statement.clearBindings();
                        statement.bindString(1, item.mId);
                        statement.bindString(2, item.mName);
                        statement.bindString(3, item.mTitle);
                        statement.executeInsert();
                    }
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
            } catch (Exception e) {
                LogUtils.e(TAG, "", e);
            }
        }

        EventBus.getDefault().post(new FollowedFuturesRefreshedEvent());
    }

    public void addFollowedFutures(FuturesInfo item) {
        if (isFollowedFutures(item.mId)) {
            return;
        }

        mFollowedFutures.add(0, item);
        try {
            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.COLUMN_FUTURES_ID, item.mId);
            contentValues.put(DbHelper.COLUMN_FUTURES_NAME, item.mName);
            contentValues.put(DbHelper.COLUMN_FUTURES_TITLE, item.mTitle);
            database.insert(DbHelper.FOLLOWED_TABLE, null, contentValues);
        } catch (Exception e) {
            LogUtils.e(TAG, "", e);
        }
        EventBus.getDefault().post(new FollowedFuturesAddedEvent(item));
    }

    private static class DbHelper extends SQLiteOpenHelper {
        private static final String TAG = DbHelper.class.getSimpleName();
        private static final String DB_NAME = "data.db";
        private static final int DB_VERSION = 1;
        public static final String FOLLOWED_TABLE = "followed";
        public static final String COLUMN_FUTURES_ID = "futures_id";
        public static final String COLUMN_FUTURES_NAME = "futures_name";
        public static final String COLUMN_FUTURES_TITLE = "futures_title";

        public DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS followed(" +
                        "futures_id TEXT PRIMARY KEY," +
                        "futures_name TEXT," +
                        "futures_title TEXT," +
                        "insert_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            } catch (SQLException e) {
                LogUtils.e(TAG, "", e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // super.onDowngrade(db, oldVersion, newVersion);
        }
    }
}
