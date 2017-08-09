package com.alapshin.mvp.view;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpController;
import com.alapshin.arctor.view.MvpView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseController<V extends MvpView, P extends Presenter<V>>
        extends MvpController<V, P> {
    Unbinder unbinder;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        injectDependencies();
        View view = inflater.inflate(getLayoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onDestroyView(View view) {
        super.onDestroyView(view);
        unbinder.unbind();
    }

    @LayoutRes
    protected abstract int getLayoutRes();
    protected abstract void injectDependencies();
}
