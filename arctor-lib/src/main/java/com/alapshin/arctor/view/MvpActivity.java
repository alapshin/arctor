package com.alapshin.arctor.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.alapshin.arctor.base.BaseActivity;
import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.presenter.PresenterDelegate;

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
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate.onCreate(presenter, savedInstanceState);
        presenterDelegate.onViewCreated((V) this);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        presenterDelegate.onStart();
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        presenterDelegate.onResume();
    }

    @Override
    @CallSuper
    protected void onPause() {
        super.onPause();
        presenterDelegate.onPause();
    }

    @Override
    @CallSuper
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterDelegate.onSaveInstanceState(outState);
    }

    @Override
    @CallSuper
    protected void onStop() {
        super.onStop();
        presenterDelegate.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroyView();
        presenterDelegate.onDestroy();
    }
}
