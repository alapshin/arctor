package com.alapshin.arctor.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alapshin.arctor.view.MvpView;


/**
 * Presenter interface
 */
public interface Presenter<V extends MvpView> {
    void onCreate(@Nullable PresenterBundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onSaveInstanceState(@NonNull PresenterBundle outState);
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
