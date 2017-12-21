package com.shuai.futures.utils;

/**
 *
 */

public class StockUtil {
    public enum StockType{
        Futures,
        Stock
    }

    public static StockType getStockType(String name){
        name=name.toLowerCase();
        if(name.startsWith("sh")||name.startsWith("sz")){
            return StockType.Stock;
        }

        return StockType.Futures;
    }

}
