package com.alapshin.arctor.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alapshin.arctor.delegate.FragmentMvpDelegate;
import com.alapshin.arctor.delegate.FragmentMvpDelegateImpl;
import com.alapshin.arctor.delegate.MvpCallback;
import com.alapshin.arctor.presenter.Presenter;

import javax.inject.Inject;

public abstract class MvpFragment<V extends MvpView, P extends Presenter<V>>
        extends Fragment
        implements MvpCallback<V, P>, MvpView {
    @Inject
    protected P presenter;
    private FragmentMvpDelegate<V, P> mvpDelegate = new FragmentMvpDelegateImpl<>(this);

    @Override
    public V getMvpView() {
        return (V) this;
    }

    @Override
    public P getPresenter() {
        return presenter;
    }

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpDelegate.onViewCreated(savedInstanceState);
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
