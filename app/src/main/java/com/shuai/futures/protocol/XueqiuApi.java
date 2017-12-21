package com.shuai.futures.protocol;

import com.shuai.futures.data.KlineItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 雪球api
 */
public interface XueqiuApi {
    @XueqiuKlineItem
    @GET("stock/forchartk/stocklist.json")
    Observable<List<KlineItem>> getKline(@Query("symbol")String symbol,
                                         @Query("period")String period,
                                         @Query("type")String type,
                                         @Query("end")long end,
                                         @Query("count")int count,
                                         @Header("Cookie") String cookie);
}
