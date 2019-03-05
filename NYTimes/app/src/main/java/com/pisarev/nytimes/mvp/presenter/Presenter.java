package com.pisarev.nytimes.mvp.presenter;

import com.pisarev.nytimes.Const;
import com.pisarev.nytimes.application.App;
import com.pisarev.nytimes.mvp.model.model_result.Result;
import com.pisarev.nytimes.mvp.model.model_result.ResultList;
import com.pisarev.nytimes.mvp.model.retrofit.Api;
import com.pisarev.nytimes.mvp.model.retrofit.RetroClient;
import com.pisarev.nytimes.mvp.View;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Presenter implements View.PresenterMvp, Observer<ResultList> {

    @Inject
    Api apiService;
    private static Subscription[] subscription = new Subscription[Const.SECTION.length];

    private View.MainMvp viewMvp;
    private int numberTab;

    public Presenter(View.MainMvp viewMvp, int numberTab) {
        this.viewMvp = viewMvp;
        this.numberTab = numberTab;
    }

    @Override
    public void onCreateView() {
        App.getComponent().inject( this );
        if (subscription[numberTab] != null && !subscription[numberTab].isUnsubscribed()) {
            subscription[numberTab].unsubscribe();
        }
        subscribe( RetroClient.getModelsObservable( numberTab ), this );
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        viewMvp.showError( e );
    }

    @Override
    public void onNext(ResultList resultList) {
        viewMvp.getResult( getSectionFilter( Const.SECTION[numberTab], resultList.getResults() ) );
    }

    @Override
    public void onRefresh() {
        if (subscription[numberTab] != null && !subscription[numberTab].isUnsubscribed()) {
            subscription[numberTab].unsubscribe();
        }
        RetroClient.resetModelsObservable();
        subscribe( RetroClient.getModelsObservable( numberTab ), this );
    }

    @Override
    public void onDestroy() {
        if (subscription != null && !subscription[numberTab].isUnsubscribed())
            subscription[numberTab].unsubscribe();
    }

    private void subscribe(Observable<ResultList> observable, Observer<ResultList> subscriber) {
        subscription[numberTab] = observable.subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( subscriber );
    }

    private ArrayList<Result> getSectionFilter(String section, ArrayList<Result> list) {
        ArrayList<Result> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get( i ).getSection().equals( section ))
                temp.add( list.get( i ) );
        }
        return temp;
    }


}
