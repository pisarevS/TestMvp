package com.pisarev.nytimes.mvp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pisarev.nytimes.Const;
import com.pisarev.nytimes.R;
import com.pisarev.nytimes.adapter.ResultListAdapter;
import com.pisarev.nytimes.mvp.model.model_result.Result;
import com.pisarev.nytimes.mvp.View;
import com.pisarev.nytimes.mvp.presenter.Presenter;

import java.util.ArrayList;

public class MyFragment extends Fragment implements View.MainMvp, SwipeRefreshLayout.OnRefreshListener {

    private ArrayList[] list = new ArrayList[Const.SECTION.length];
    private int numberTab;
    private boolean[] isLoading = new boolean[Const.SECTION.length];
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View.PresenterMvp presenter;

    public MyFragment() {
        for (int i = 0; i < Const.SECTION.length; i++)
            list[i] = new ArrayList<Result>();
    }

    public static MyFragment newInstance(int numberTab) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt( Const.NUMBER_TAB, numberTab );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        Const.isFavorites = false;
        android.view.View rootView = inflater.inflate( R.layout.fragment_main, container, false );
        numberTab = getArguments().getInt( Const.NUMBER_TAB );
        presenter = new Presenter( this, numberTab );
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey( Const.KEY_MODELS + numberTab )) {
                list[numberTab] = savedInstanceState.getParcelableArrayList( Const.KEY_MODELS + numberTab );
                isLoading[numberTab] = savedInstanceState.getBoolean( Const.KEY_IS_LOADING + numberTab );
            }
        }
        if (list[numberTab].size() == 0 || isLoading[numberTab]) {
            presenter.onCreateView();
        }
        swipeRefreshLayout = rootView.findViewById( R.id.swipeRefreshLayout );
        swipeRefreshLayout.setOnRefreshListener( this );
        recyclerView = rootView.findViewById( R.id.recyclerView );
        recyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) );
        recyclerView.setAdapter( new ResultListAdapter( getContext(), list[numberTab] ) );
        return rootView;
    }


    @Override
    public void getResult(ArrayList<Result> resultArrayList) {
        list[numberTab].clear();
        list[numberTab].addAll( resultArrayList );
        if (isAdded()) {
            recyclerView.getAdapter().notifyItemRangeInserted( 0, list[numberTab].size() );
        }
        recyclerView.setAdapter( new ResultListAdapter( getContext(), list[numberTab] ) );
    }

    @Override
    public void showError(Throwable e) {
        Toast.makeText( getContext(), e.getMessage(), Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
        swipeRefreshLayout.setRefreshing( false );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        if (!ResultListAdapter.isTouch) {
            for (int i = 0; i < Const.SECTION.length; i++) {
                outState.putParcelableArrayList( Const.KEY_MODELS + i, list[i] );
                outState.putBoolean( Const.KEY_IS_LOADING + i, isLoading[i] );
            }
        }
        ResultListAdapter.isTouch = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.onDestroy();
    }
}