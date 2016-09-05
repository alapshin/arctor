package com.alapshin.arctor.delegate;

import android.os.Parcelable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

public class ViewGroupMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements ViewGroupMvpDelegate<V, P> {

    private MvpCallback<V, P> callback;

    public ViewGroupMvpDelegateImpl(MvpCallback<V, P> callback) {
        this.callback = callback;
    }

    @Override
    public void onAttachedToWindow() {
        callback.getPresenter().onCreate(null);
        callback.getPresenter().attachView(callback.getMvpView());
        callback.getPresenter().onStart();
        callback.getPresenter().onResume();
    }

    @Override
    public void onDetachedFromWindow() {
        callback.getPresenter().detachView();
        callback.getPresenter().onPause();
        callback.getPresenter().onStop();
        callback.getPresenter().onDestroy();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return null;
    }
}
