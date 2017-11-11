package com.shuai.futures.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 */
public class IoUtils {
    public static void closeQuietly(Cursor cursor) {
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {

        }
    }

    public static void closeQuietly(SQLiteDatabase database) {
        try {
            if (database != null) {
                database.close();
            }
        } catch (Exception e) {

        }
    }
}
