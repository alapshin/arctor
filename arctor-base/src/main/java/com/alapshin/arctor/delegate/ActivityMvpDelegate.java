package com.alapshin.arctor.delegate;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

public interface ActivityMvpDelegate<V extends MvpView, P extends Presenter<V>> {
    void onCreate(@Nullable Bundle savedInstanceState);
    void onContentChanged();
    void onStart();
    void onResume();
    void onPause(boolean isFinishing);
    void onSaveInstanceState(@Nullable Bundle outState);
    void onStop();
    void onDestroy(boolean isChangingConfigurations);
}
