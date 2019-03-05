package com.pisarev.nytimes.di.component;

import com.pisarev.nytimes.di.module.AppModule;
import com.pisarev.nytimes.mvp.presenter.Presenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(Presenter presenter);
}
