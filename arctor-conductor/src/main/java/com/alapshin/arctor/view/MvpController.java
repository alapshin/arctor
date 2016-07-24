package com.alapshin.arctor.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alapshin.arctor.delegate.ControllerMvpDelegate;
import com.alapshin.arctor.delegate.ControllerMvpDelegateImpl;
import com.alapshin.arctor.delegate.MvpCallback;
import com.alapshin.arctor.delegate.MvpControllerLifecycleListener;
import com.alapshin.arctor.presenter.Presenter;
import com.bluelinelabs.conductor.Controller;

import javax.inject.Inject;

public abstract class MvpController<V extends MvpView, P extends Presenter<V>>
        extends Controller
        implements MvpCallback<V, P>, MvpView {

    @Inject
    protected P presenter;

    private ControllerMvpDelegate<V, P> mvpDelegate;
    private final MvpControllerLifecycleListener<V, P> lifecycleListener;

    public MvpController() {
        this(null);
    }

    public MvpController(Bundle savedInstanceState) {
        super(savedInstanceState);
        mvpDelegate = new ControllerMvpDelegateImpl<>(this);
        lifecycleListener =
                new MvpControllerLifecycleListener<>(mvpDelegate);
        addLifecycleListener(lifecycleListener);
    }

    @Override
    public V getMvpView() {
        return (V) this;
    }

    @Override
    public P getPresenter() {
        return presenter;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mvpDelegate.onSaveInstanceState(outState);
    }
}
