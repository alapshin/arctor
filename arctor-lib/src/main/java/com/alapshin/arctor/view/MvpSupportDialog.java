package com.alapshin.arctor.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;

import com.alapshin.arctor.base.BaseSupportDialogFragment;
import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.presenter.PresenterDelegate;

import javax.inject.Inject;

/**
 * @author alapshin
 * @since 2015-08-30
 */
public abstract class MvpSupportDialog<V extends MvpView, P extends Presenter<V>> extends BaseSupportDialogFragment
        implements MvpView {
    protected @Inject P presenter;
    private PresenterDelegate<V, P> presenterDelegate = new PresenterDelegate<>();

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate.onCreate(presenter, savedInstanceState);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenterDelegate.onViewCreated((V) this);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        presenterDelegate.onStart();
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        presenterDelegate.onResume();
    }

    @Override
    @CallSuper
    public void onPause() {
        presenterDelegate.onPause();
        super.onPause();
    }

    @Override
    @CallSuper
    public void onSaveInstanceState(Bundle outState) {
        presenterDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    @CallSuper
    public void onStop() {
        presenterDelegate.onStop();
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        presenterDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        presenterDelegate.onDestroy();
        super.onDestroy();
    }
}
