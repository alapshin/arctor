package com.alapshin.mvp.view;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpController;
import com.alapshin.arctor.view.MvpView;

public abstract class BaseController<V extends MvpView, P extends Presenter<V>>
        extends MvpController<V, P> {

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        injectDependencies();
        mvpDelegate.onCreateView((V) this, presenter);
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @LayoutRes
    protected abstract int getLayoutRes();
    protected abstract void injectDependencies();
}
