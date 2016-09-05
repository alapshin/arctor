package com.alapshin.arctor.presenter;

import com.alapshin.arctor.view.MvpView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Presenter interface
 */
public interface Presenter<V extends MvpView> {
    void onCreate(@Nullable PresenterBundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onSaveInstanceState(@Nonnull PresenterBundle outState);
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
