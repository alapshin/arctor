package com.alapshin.arctor.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alapshin.arctor.base.BaseFragment;
import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.presenter.PresenterDelegate;

import javax.inject.Inject;


/**
 * @author alapshin
 * @since 2015-04-18
 */
public abstract class MvpFragment<V extends MvpView, P extends Presenter<V>> extends BaseFragment
        implements MvpView {
    protected @Inject P presenter;
    private PresenterDelegate<V, P> presenterDelegate = new PresenterDelegate<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate.onCreate(presenter, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenterDelegate.onViewCreated((V) this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenterDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterDelegate.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenterDelegate.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroy();
    }
}
