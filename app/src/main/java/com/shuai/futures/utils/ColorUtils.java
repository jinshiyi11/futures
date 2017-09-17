package com.shuai.futures.utils;

/**
 *
 */

public class ColorUtils {

    public static String toHtmlColor(int color) {
        return String.format("#%s", Integer.toHexString(color & 0x00ffffff));
    }
}
