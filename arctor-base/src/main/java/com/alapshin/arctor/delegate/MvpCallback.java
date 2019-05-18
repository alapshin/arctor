package com.alapshin.arctor.delegate;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

public interface MvpCallback<V extends MvpView, P extends Presenter<V>> {
    V getMvpView();
    P getPresenter();
}
