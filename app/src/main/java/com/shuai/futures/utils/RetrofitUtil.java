package com.shuai.futures.utils;

import com.shuai.futures.protocol.XueqiuKlineConverter;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class RetrofitUtil {
    private static Retrofit mRetrofit = createRetrofit();

    private static Retrofit createRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://xueqiu.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new XueqiuKlineConverter.Factory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getRetrofit(){
        return mRetrofit;
    }

}
