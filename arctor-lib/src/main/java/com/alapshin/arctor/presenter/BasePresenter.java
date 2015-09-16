package com.alapshin.arctor.presenter;

import android.util.Log;

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
    public void onCreate(@Nullable PresenterBundle savedInstanceState) {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
    }

    @Override
    public void onSaveInstanceState(@Nonnull PresenterBundle outState) {
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void attachView(V view) {
        Log.d(TAG, "attachView");
        this.viewRef = new WeakReference<V>(view);
    }

    @Override
    public void detachView() {
        Log.d(TAG, "detachView");
        viewRef = null;
    }

    protected boolean isViewAttached() {
        return viewRef != null;
    }

    protected V getView() {
        return viewRef == null ? null : viewRef.get();
    }
}
