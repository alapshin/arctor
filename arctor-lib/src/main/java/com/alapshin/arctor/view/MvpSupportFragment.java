package com.alapshin.arctor.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.alapshin.arctor.base.BaseSupportFragment;
import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.presenter.PresenterDelegate;

import javax.inject.Inject;

/**
 * @author alapshin
 * @since 2015-08-30
 */
public abstract class MvpSupportFragment<V extends MvpView, P extends Presenter<V>> extends BaseSupportFragment
        implements MvpView {
    private final String TAG = this.getClass().getSimpleName();

    protected @Inject P presenter;
    private PresenterDelegate<V, P> presenterDelegate = new PresenterDelegate<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
        presenterDelegate.onCreate(presenter, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated");
        presenterDelegate.onViewCreated((V) this);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");
        presenterDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
        presenterDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");
        presenterDelegate.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");
        presenterDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "onStop");
        presenterDelegate.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG, "onDestroyView");
        presenterDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");
        presenterDelegate.onDestroy();
    }
}
