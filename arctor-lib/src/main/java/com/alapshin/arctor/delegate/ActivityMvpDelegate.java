package com.alapshin.arctor.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

import javax.annotation.Nonnull;

public interface ActivityMvpDelegate<V extends MvpView, P extends Presenter<V>> {
    public void onCreate(@Nonnull V view, @Nonnull P presenter, @Nullable Bundle savedInstanceState);
    public void onStart();
    public void onResume();
    public void onPause();
    public void onSaveInstanceState(@Nullable Bundle outState);
    public void onStop();
    public void onDestroy();
}
