package com.alapshin.arctor.view;

import android.support.annotation.CallSuper;

import com.alapshin.arctor.presenter.Presenter;

/**
 * @author alapshin
 * @since 2015-07-04
 */
public abstract class MvpLceActivity<D, V extends MvpView, P extends Presenter<V>>
        extends MvpActivity<V, P> implements MvpLceView<D> {
    @Override
    public void setData(D data) {
    }

    @Override
    public void showError(Throwable error) {
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void showProgress(String message) {
    }

    @Override
    public void showProgress(String message, float progress) {
    }
}
