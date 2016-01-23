package com.alapshin.arctor.presenter;

import android.support.annotation.CallSuper;
import android.util.Log;

import com.alapshin.arctor.BuildConfig;
import com.alapshin.arctor.view.MvpView;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base presenter implementation
 *
 * @author alapshin
 * @since 2015-04-18
 */
public abstract class BasePresenter<V extends MvpView> implements Presenter<V> {
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Reference to view. Using weak reference to avoid memory leaks. Before calling any view
     * methods check that view is attached with isViewAttached
     */
    protected WeakReference<V> viewRef;

    @Override
    @CallSuper
    public void onCreate(@Nullable PresenterBundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate");
        }
    }

    @Override
    @CallSuper
    public void onStart() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStart");
        }
    }

    @Override
    @CallSuper
    public void onResume() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onResume");
        }
    }

    @Override
    @CallSuper
    public void onPause() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPause");
        }
    }

    @Override
    @CallSuper
    public void onSaveInstanceState(@Nonnull PresenterBundle outState) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onSaveInstanceState");
        }
    }

    @Override
    @CallSuper
    public void onStop() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStop");
        }
    }

    @Override
    @CallSuper
    public void onDestroy() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDestroy");
        }
    }

    @Override
    @CallSuper
    public void attachView(V view) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "attachView");
        }
        this.viewRef = new WeakReference<V>(view);
    }

    @Override
    @CallSuper
    public void detachView() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "detachView");
        }
        viewRef.clear();
        viewRef = null;
    }

    public boolean isViewAttached() {
        return viewRef != null;
    }

    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }
}
