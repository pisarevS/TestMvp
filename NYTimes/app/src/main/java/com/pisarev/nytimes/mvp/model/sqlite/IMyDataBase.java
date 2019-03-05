package com.pisarev.nytimes.mvp.model.sqlite;

import com.pisarev.nytimes.mvp.model.model_result.Result;

import java.util.ArrayList;

public interface IMyDataBase {

    ArrayList<Result> getResultList();
    void addResult(Result result);
    void deleteItemResult(String value);
}

