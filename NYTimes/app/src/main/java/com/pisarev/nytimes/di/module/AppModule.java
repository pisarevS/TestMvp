package com.pisarev.nytimes.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pisarev.nytimes.Const;
import com.pisarev.nytimes.mvp.model.retrofit.Api;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module
public class AppModule {

    private Context context;
    public AppModule(Context context) {
        this.context=context;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return context;
    }

    @Provides
    @Singleton
    Api provideApi(Retrofit retrofit) {
        return retrofit.create( Api.class );
    }

    @Singleton
    @Provides
    @Named("ok-1")
    OkHttpClient provideOkHttpClient1() {
        return new OkHttpClient.Builder()
                .connectTimeout( 20, TimeUnit.SECONDS )
                .readTimeout( 20, TimeUnit.SECONDS )
                .build();
    }

    @Singleton
    @Provides
    @Named("ok-2")
    OkHttpClient provideOkHttpClient2() {
        return new OkHttpClient.Builder()
                .connectTimeout( 60, TimeUnit.SECONDS )
                .readTimeout( 60, TimeUnit.SECONDS )
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    RxJavaCallAdapterFactory rxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.createWithScheduler( Schedulers.io() );
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@Named("ok-1") OkHttpClient okHttpClient, RxJavaCallAdapterFactory rxAdapter, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl( Const.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create( gson ) )
                .addCallAdapterFactory( rxAdapter )
                .client( okHttpClient )
                .build();
    }
}
