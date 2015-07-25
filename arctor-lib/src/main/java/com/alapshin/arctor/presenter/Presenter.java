package com.alapshin.arctor.presenter;

import com.alapshin.arctor.view.MvpView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Presenter interface
 *
 * @author alapshin
 * @since 2015-04-18
 */
public interface Presenter<V extends MvpView> {
    void onCreate(@Nullable PresenterBundle bundle);
    void onStart();
    void onResume();
    void onPause();
    void onSaveInstanceState(@Nonnull PresenterBundle bundle);
    void onStop();
    void onDestroy();

    /**
     * Attach view to presenter
     * @param view view to attach
     */
    void attachView(V view);

    /**
     * Detach view from presenter
     */
    void detachView();
}
