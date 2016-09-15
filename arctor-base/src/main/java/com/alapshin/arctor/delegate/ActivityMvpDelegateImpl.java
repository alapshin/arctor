package com.alapshin.arctor.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.view.MvpView;

import static com.alapshin.arctor.presenter.PresenterBundleUtil.getPresenterBundle;
import static com.alapshin.arctor.presenter.PresenterBundleUtil.setPresenterBundle;

public class ActivityMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements ActivityMvpDelegate<V, P> {

    private MvpCallback<V, P> callback;
    private boolean isDestroyedBySystem;

    public ActivityMvpDelegateImpl(MvpCallback<V, P> callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        callback.getPresenter().onCreate(
                getPresenterBundle(savedInstanceState));
    }

    @Override
    public void onContentChanged() {
        callback.getPresenter().attachView(callback.getMvpView());
    }

    @Override
    public void onStart() {
        callback.getPresenter().onStart();
    }

    @Override
    public void onResume() {
        isDestroyedBySystem = false;
        callback.getPresenter().onResume();
    }

    @Override
    public void onPause() {
        callback.getPresenter().onPause();
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        isDestroyedBySystem = true;
        PresenterBundle bundle = new PresenterBundle();
        callback.getPresenter().onSaveInstanceState(bundle);
        setPresenterBundle(outState, bundle);
    }

    @Override
    public void onStop() {
        callback.getPresenter().onStop();
    }

    @Override
    public void onDestroy() {
        callback.getPresenter().detachView();
        if (!isDestroyedBySystem) {
            callback.getPresenter().onDestroy();
        }
    }
}
