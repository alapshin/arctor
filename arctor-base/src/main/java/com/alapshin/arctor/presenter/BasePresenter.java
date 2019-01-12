package com.alapshin.arctor.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alapshin.arctor.view.MvpView;

import java.lang.ref.WeakReference;

/**
 * Base presenter implementation
 *
 * @author alapshin
 * @since 2015-04-18
 */
public class BasePresenter<V extends MvpView> implements Presenter<V> {
    private boolean orientationChanged = false;
    private boolean viewOnCreateCalled = false;
    /**
     * Reference to view.
     * Using weak reference to avoid memory leaks.
     * Before calling any view methods check that view is attached with isViewAttached
     */
    protected WeakReference<V> viewRef;

    @Override
    @CallSuper
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable PresenterBundle savedInstanceState) {
        // Presenter's onCreate method could be called in 3 different cases
        // 1. After corresponding view is created for the first time
        // 2. After corresponding view is recreated after configuration change
        // 3. After corresponding view is recreated after process death

        // In first case savedInstanceState is null, while in second and third case it is not.
        // As a result to distinguish between later two cases checking for savedInstanceState alone is not enough.
        // To solve this problem additional flag is introduced that could be checked when savedInstanceState is not null.
        // When presenter's onCreate is called after configuration change this flag will be true and false otherwise.
        if (viewOnCreateCalled) {
           orientationChanged = true;
        } else {
            viewOnCreateCalled = true;
            orientationChanged = false;
        }
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
    public void onSaveInstanceState(@NonNull PresenterBundle outState) {
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
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    /**
     * Return currently attached view
     * @return attached view view or null if view is detached
     */
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    public boolean isOrientationChanged() {
        return orientationChanged;
    }
}
