package com.alapshin.arctor.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

public interface ActivityMvpDelegate<V extends MvpView, P extends Presenter<V>> {
    void onCreate(@Nullable Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onSaveInstanceState(@Nullable Bundle outState);
    void onStop();
    void onDestroy();
}