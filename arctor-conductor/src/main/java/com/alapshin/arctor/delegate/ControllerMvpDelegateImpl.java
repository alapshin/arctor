package com.alapshin.arctor.delegate;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.view.MvpView;

import static com.alapshin.arctor.presenter.PresenterBundleUtil.getPresenterBundle;
import static com.alapshin.arctor.presenter.PresenterBundleUtil.setPresenterBundle;

public class ControllerMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements ControllerMvpDelegate<V, P> {

    private MvpCallback<V, P> callback;

    public ControllerMvpDelegateImpl(MvpCallback<V, P> callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        callback.getPresenter().onCreate(getPresenterBundle(savedInstanceState));
    }

    @Override
    public void onCreateView() {
        callback.getPresenter().attachView(callback.getMvpView());
    }

    @Override
    public void onAttach() {
        callback.getPresenter().onStart();
        callback.getPresenter().onResume();
    }

    @Override
    public void onDetach() {
        callback.getPresenter().onPause();
        callback.getPresenter().onStop();
    }

    @Override
    public void onDestroyView() {
        callback.getPresenter().detachView();
    }

    @Override
    public void onDestroy() {
        callback.getPresenter().onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        PresenterBundle bundle = new PresenterBundle();
        callback.getPresenter().onSaveInstanceState(bundle);
        setPresenterBundle(outState, bundle);
    }
}
