package com.alapshin.mvp.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpActivity;
import com.alapshin.arctor.view.MvpView;

import butterknife.ButterKnife;

public abstract class BaseActivity<V extends MvpView, P extends Presenter<V>>
        extends MvpActivity<V, P> {
    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        setContentView(getLayoutRes());
        super.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    public void onContentChanged() {
        ButterKnife.bind(this);
        super.onContentChanged();
    }

    @LayoutRes
    protected abstract int getLayoutRes();
    protected abstract void injectDependencies();
}
