package com.pisarev.nytimes.mvp;

import com.pisarev.nytimes.mvp.model.model_result.Result;

import java.util.ArrayList;

public interface View {

    interface MainMvp {
        void getResult(ArrayList<Result> resultArrayList);
        void showError(Throwable e);
    }

    interface PresenterMvp {
        void onCreateView();
        void onDestroy();
        void onRefresh();


    }
}

