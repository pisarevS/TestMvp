package com.pisarev.nytimes.application;

import android.app.Application;

import com.pisarev.nytimes.di.component.AppComponent;
import com.pisarev.nytimes.di.component.DaggerAppComponent;
import com.pisarev.nytimes.di.module.AppModule;
import com.pisarev.nytimes.mvp.model.retrofit.RetroClient;


public class App extends Application {

    private static AppComponent component;
    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RetroClient.init();
        component = provideComponent();
    }

    AppComponent provideComponent() {
        return component = DaggerAppComponent
                .builder().appModule( new AppModule(this) )
                .build();
    }


}
