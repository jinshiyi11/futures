package com.shuai.futures.utils;


import android.util.Log;

public abstract class SafeRunnable implements Runnable {

    @Override
    final public void run() {
        try {
            doRun();
        }catch (Exception e){
            Log.e("",e.toString());
        }
    }

    public abstract void doRun();
}
