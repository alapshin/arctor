package com.alapshin.arctor.delegate;


import android.support.annotation.NonNull;
import android.view.View;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;
import com.bluelinelabs.conductor.Controller;

public class MvpControllerLifecycleListener<V extends MvpView, P extends Presenter<V>>
        extends Controller.LifecycleListener {

    private boolean  created;
    private final ControllerMvpDelegate<V, P> delegate;

    public MvpControllerLifecycleListener(ControllerMvpDelegate<V, P> delegate) {
        this.created = false;
        this.delegate = delegate;
    }

    @Override
    public void postCreateView(@NonNull Controller controller, @NonNull View view) {
        if (!created) {
            created = true;
            delegate.onCreate(controller.getArgs());
        }
        delegate.onCreateView();
    }

    @Override
    public void postAttach(@NonNull Controller controller, @NonNull View view) {
        delegate.onAttach();
    }

    @Override
    public void preDetach(@NonNull Controller controller, @NonNull View view) {
        delegate.onDetach();
    }

    @Override
    public void preDestroyView(@NonNull Controller controller, @NonNull View view) {
        delegate.onDestroyView();
    }

    @Override
    public void preDestroy(@NonNull Controller controller) {
        created = false;
        delegate.onDestroy();
    }
}
