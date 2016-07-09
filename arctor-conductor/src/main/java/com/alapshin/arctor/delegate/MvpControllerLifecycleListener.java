package com.alapshin.arctor.delegate;


import android.support.annotation.NonNull;
import android.view.View;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;
import com.bluelinelabs.conductor.Controller;

public class MvpControllerLifecycleListener<V extends MvpView, P extends Presenter<V>>
        extends Controller.LifecycleListener {
    protected final ControllerMvpDelegate<V, P> delegate;

    public MvpControllerLifecycleListener(ControllerMvpDelegate<V, P> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void postCreateView(@NonNull Controller controller, @NonNull View view) {
    }

    @Override
    public void preDestroyView(@NonNull Controller controller, @NonNull View view) {
        delegate.onDetach();
    }
}
