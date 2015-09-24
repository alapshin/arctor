package com.alapshin.arctor.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;

import com.alapshin.arctor.base.BaseFragment;
import com.alapshin.arctor.delegate.FragmentMvpDelegate;
import com.alapshin.arctor.delegate.FragmentMvpDelegateImpl;
import com.alapshin.arctor.presenter.Presenter;

import javax.inject.Inject;

/**
 * @author alapshin
 * @since 2015-08-30
 */
public abstract class MvpFragment<V extends MvpView, P extends Presenter<V>>
        extends BaseFragment implements MvpView {

    protected @Inject P presenter;
    private FragmentMvpDelegate<V, P> mvpDelegate = new FragmentMvpDelegateImpl<>();

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate.onCreate(presenter, savedInstanceState);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpDelegate.onViewCreated((V) this, savedInstanceState);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        mvpDelegate.onStart();
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        mvpDelegate.onResume();
    }

    @Override
    @CallSuper
    public void onPause() {
        mvpDelegate.onPause();
        super.onPause();
    }

    @Override
    @CallSuper
    public void onSaveInstanceState(Bundle outState) {
        mvpDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    @CallSuper
    public void onStop() {
        mvpDelegate.onStop();
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        mvpDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        mvpDelegate.onDestroy();
        super.onDestroy();
    }
}
