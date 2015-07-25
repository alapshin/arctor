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
    /**
     * Reference to view. Using weak reference to avoid memory leaks. Before calling any view
     * methods check that view is attached with isViewAttached
     */
    protected WeakReference<V> viewRef;

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        Log.d(getClass().getSimpleName(), "onCreate");
    }

    @Override
    public void onStart() {
        Log.d(getClass().getSimpleName(), "onStart");
    }

    @Override
    public void onResume() {
        Log.d(getClass().getSimpleName(), "onResume");
    }

    @Override
    public void onPause() {
        Log.d(getClass().getSimpleName(), "onPause");
    }

    @Override
    public void onSaveInstanceState(@Nonnull PresenterBundle bundle) {
        Log.d(getClass().getSimpleName(), "onSaveInstanceState");
    }

    @Override
    public void onStop() {
        Log.d(getClass().getSimpleName(), "onStop");
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getSimpleName(), "onDestroy");
    }

    @Override
    public void attachView(V view) {
        this.viewRef = new WeakReference<V>(view);
        Log.d(getClass().getSimpleName(), "attachView");
    }

    @Override
    public void detachView() {
        viewRef = null;
        Log.d(getClass().getSimpleName(), "detachView");
    }

    protected boolean isViewAttached() {
        return viewRef != null;
    }

    protected V getView() {
        return viewRef == null ? null : viewRef.get();
    }
}
