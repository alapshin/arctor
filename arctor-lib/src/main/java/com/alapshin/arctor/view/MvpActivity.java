package com.alapshin.arctor.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.alapshin.arctor.base.BaseActivity;
import com.alapshin.arctor.delegate.ActivityMvpDelegate;
import com.alapshin.arctor.delegate.ActivityMvpDelegateImpl;
import com.alapshin.arctor.presenter.Presenter;

import javax.inject.Inject;

public abstract class MvpActivity<V extends MvpView, P extends Presenter<V>> extends BaseActivity
        implements MvpView {
    protected @Inject P presenter;
    private ActivityMvpDelegate<V, P> mvpDelegate = new ActivityMvpDelegateImpl<>();

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate.onCreate((V) this, presenter, savedInstanceState);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        mvpDelegate.onStart();
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        mvpDelegate.onResume();
    }

    @Override
    @CallSuper
    protected void onPause() {
        mvpDelegate.onPause();
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onSaveInstanceState(Bundle outState) {
        mvpDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    @CallSuper
    protected void onStop() {
        mvpDelegate.onStop();
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        mvpDelegate.onDestroy();
        super.onDestroy();
    }
}
