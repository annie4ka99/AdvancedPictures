package com.example.advancedpictures;

import android.app.Application;
import android.util.Log;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MyApp extends Application {
    // DBHELP

    private static FlickrService api;
    public static FavouritesDBHelper helper;

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d("ACTION: ite", "onCreate App");
        helper = FavouritesDBHelper.getInstance(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.flickr.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory
                        .createWithScheduler(Schedulers.io())).build();
        api = retrofit.create(FlickrService.class);
    }

    @Override
    public void onTerminate() {
        helper.close();
        super.onTerminate();
    }

    public static FlickrService getApi() {
        return api;
    }

    public static FavouritesDBHelper getDataBase() {return helper;}

}