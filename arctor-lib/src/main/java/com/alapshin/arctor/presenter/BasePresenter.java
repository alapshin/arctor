package com.alapshin.arctor.presenter;

import android.support.annotation.CallSuper;

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
public class BasePresenter<V extends MvpView> implements Presenter<V> {
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Reference to view. Using weak reference to avoid memory leaks. Before calling any view
     * methods check that view is attached with isViewAttached
     */
    protected WeakReference<V> viewRef;

    @Override
    @CallSuper
    public void onCreate(@Nullable PresenterBundle savedInstanceState) {
    }

    @Override
    @CallSuper
    public void onStart() {
    }

    @Override
    @CallSuper
    public void onResume() {
    }

    @Override
    @CallSuper
    public void onPause() {
    }

    @Override
    @CallSuper
    public void onSaveInstanceState(@Nonnull PresenterBundle outState) {
    }

    @Override
    @CallSuper
    public void onStop() {
    }

    @Override
    @CallSuper
    public void onDestroy() {
    }

    @Override
    @CallSuper
    public void attachView(V view) {
        this.viewRef = new WeakReference<V>(view);
    }

    @Override
    @CallSuper
    public void detachView() {
        viewRef.clear();
        viewRef = null;
    }

    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }
}
