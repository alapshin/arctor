package com.alapshin.arctor.view;

import com.alapshin.arctor.presenter.Presenter;

/**
 * @author alapshin
 * @since 2015-08-30
 */
public abstract class MvpLceDialogFragment<D, V extends MvpView, P extends Presenter<V>>
        extends MvpDialogFragment<V, P> implements MvpLceView<D> {
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
