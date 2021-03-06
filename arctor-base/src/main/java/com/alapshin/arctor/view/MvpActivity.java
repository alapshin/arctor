package com.alapshin.arctor.view;

import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alapshin.arctor.delegate.ActivityMvpDelegate;
import com.alapshin.arctor.delegate.ActivityMvpDelegateImpl;
import com.alapshin.arctor.delegate.MvpCallback;
import com.alapshin.arctor.presenter.Presenter;

import javax.inject.Inject;

public abstract class MvpActivity<V extends MvpView, P extends Presenter<V>>
        extends AppCompatActivity
        implements MvpCallback<V, P>, MvpView {
    @Inject
    P presenter;
    private ActivityMvpDelegate<V, P> mvpDelegate = new ActivityMvpDelegateImpl<>(this);

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mvpDelegate.onContentChanged();
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        mvpDelegate.onStart();
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        mvpDelegate.onResume();
    }

    @Override
    @CallSuper
    protected void onPause() {
        mvpDelegate.onPause(isFinishing());
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onSaveInstanceState(Bundle outState) {
        mvpDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    @CallSuper
    protected void onStop() {
        mvpDelegate.onStop();
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        mvpDelegate.onDestroy(isChangingConfigurations());
        super.onDestroy();
    }
}
