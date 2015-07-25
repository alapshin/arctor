package com.alapshin.arctor.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alapshin.arctor.base.BaseActivity;
import com.alapshin.arctor.presenter.Presenter;

import javax.inject.Inject;


/**
 * @author alapshin
 * @since 2015-06-04
 */
public abstract class MvpActivity<V extends MvpView, P extends Presenter<V>> extends BaseActivity
        implements MvpView {
    protected @Inject P presenter;
    private PresenterDelegate<V, P> presenterDelegate = new PresenterDelegate<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate.onCreate(presenter, savedInstanceState);
        presenterDelegate.onViewCreated((V) this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenterDelegate.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenterDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterDelegate.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterDelegate.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenterDelegate.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroyView();
        presenterDelegate.onDestroy();
    }
}
