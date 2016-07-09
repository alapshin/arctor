package com.alapshin.arctor.view;


import com.alapshin.arctor.delegate.ControllerMvpDelegate;
import com.alapshin.arctor.delegate.ControllerMvpDelegateImpl;
import com.alapshin.arctor.delegate.MvpControllerLifecycleListener;
import com.alapshin.arctor.presenter.Presenter;
import com.bluelinelabs.conductor.Controller;

import javax.inject.Inject;

public abstract class MvpController<V extends MvpView, P extends Presenter<V>> extends Controller
        implements MvpView {
    @Inject
    protected P presenter;

    protected ControllerMvpDelegate<V, P> mvpDelegate = new ControllerMvpDelegateImpl<>();
    protected MvpControllerLifecycleListener<V, P> lifecycleListener =
            new MvpControllerLifecycleListener<>(mvpDelegate);

    {
        addLifecycleListener(lifecycleListener);
    }
}
