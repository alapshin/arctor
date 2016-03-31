package com.alapshin.arctor.delegate;

import android.os.Parcelable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

public class ViewGroupMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements ViewGroupMvpDelegate<V, P> {

    P presenter;

    @Override
    public void onAttachedToWindow(V view, P presenter) {
        presenter.attachView(view);
        this.presenter = presenter;
        presenter.onCreate(null);
        presenter.onStart();
        presenter.onResume();
    }

    @Override
    public void onDetachedFromWindow() {
        presenter.detachView();
        presenter.onPause();
        presenter.onStop();
        presenter.onDestroy();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return null;
    }
}
