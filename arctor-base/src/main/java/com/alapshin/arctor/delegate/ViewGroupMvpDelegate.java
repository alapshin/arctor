package com.alapshin.arctor.delegate;

import android.os.Parcelable;
import android.view.ViewGroup;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

/**
 * MvpDelegate for ViewGroup based views.
 *
 * @param <V> view type.
 * @param <P> presenter type.
 */
public interface ViewGroupMvpDelegate<V extends MvpView, P extends Presenter<V>> {
    /**
     * Call from {@link ViewGroup#onAttachedToWindow()}
     */
    void onAttachedToWindow();

    /**
     * Call from {@link ViewGroup#onDetachedFromWindow()}
     */
    void onDetachedFromWindow();

    /**
     * Call from {@link ViewGroup#onWindowVisibilityChanged(int)}
     */
    void onWindowVisibilityChanges(int visibility);

    /**
     * Call from {@link ViewGroup#onRestoreInstanceState(Parcelable)}
     * @param state The parcelable state.
     */
    void onRestoreInstanceState(Parcelable state);

    /**
     * Call from {@link ViewGroup#onSaveInstanceState()}
     * @return The state with saved data
     */
    Parcelable onSaveInstanceState();
}
