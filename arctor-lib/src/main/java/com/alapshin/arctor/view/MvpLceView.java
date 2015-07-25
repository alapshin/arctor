package com.alapshin.arctor.view;

import com.alapshin.arctor.presenter.Presenter;

/**
 * @author alapshin
 * @since 2015-07-04
 */
public interface MvpLceView<D> extends MvpView {
    void setData(D data);
    void showError(Throwable error);
    void showProgress();
    void showProgress(String message);
    void showProgress(String message, float progress);
}
