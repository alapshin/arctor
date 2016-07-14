package com.alapshin.arctor.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

import javax.annotation.Nonnull;

public interface FragmentMvpDelegate<V extends MvpView, P extends Presenter<V>> {
    public void onCreate(@Nullable Bundle savedInstanceState);
    public void onViewCreated(@Nullable Bundle savedInstanceState);
    public void onStart();
    public void onResume();
    public void onPause();
    public void onSaveInstanceState(Bundle outState);
    public void onStop();
    public void onDestroyView();
    public void onDestroy();
}
