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
        callback.getPresenter().onResume();
    }

    @Override
    public void onPause(boolean isFinishing) {
        callback.getPresenter().onPause();
        // If activity is finishing then mark view as detached.
        // Otherwise there is possibility that view will still receive
        // some event while another activity already in foreground.
        if (isFinishing) {
            callback.getPresenter().detachView();
        }
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        PresenterBundle bundle = new PresenterBundle();
        // Get presenter bundle
        callback.getPresenter().onSaveInstanceState(bundle);
        // Write presenter bundle to view bundle
        setPresenterBundle(outState, bundle);
    }

    @Override
    public void onStop() {
        callback.getPresenter().onStop();
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        callback.getPresenter().detachView();
        // If activity is destroyed then notify presenter about that.
        if (!isChangingConfigurations) {
            callback.getPresenter().onDestroy();
        }
    }
}
