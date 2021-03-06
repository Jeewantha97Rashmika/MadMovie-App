package com.chathura.madmovie.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;



    public RetrofitClient() {
    }

    public static Retrofit getInstance() {
        if(instance == null)
            instance = new Retrofit.Builder().baseUrl("http://suggestqueries.google.com/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
}
