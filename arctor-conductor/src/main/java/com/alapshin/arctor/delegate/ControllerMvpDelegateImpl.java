package com.alapshin.arctor.delegate;


import android.support.annotation.NonNull;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

public class ControllerMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements ControllerMvpDelegate<V, P> {

    private P presenter;

    @Override
    public void onCreateView(@NonNull V view, @NonNull P presenter) {
        this.presenter = presenter;
        presenter.attachView(view);
    }

    @Override
    public void onDetach() {
        presenter.detachView();
    }
}
