package com.alapshin.arctor.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.alapshin.arctor.delegate.MvpCallback;
import com.alapshin.arctor.delegate.ViewGroupMvpDelegate;
import com.alapshin.arctor.delegate.ViewGroupMvpDelegateImpl;
import com.alapshin.arctor.presenter.Presenter;

import javax.inject.Inject;

public abstract class MvpFrameLayout<V extends MvpView, P extends Presenter<V>>
        extends FrameLayout
        implements MvpCallback<V, P>, MvpView {
    private static final String PARENT_STATE_KEY = "parent_state";

    @Inject
    P presenter;
    private ViewGroupMvpDelegate<V, P> mvpDelegate = new ViewGroupMvpDelegateImpl<>(this);

    public MvpFrameLayout(Context context) {
        super(context);
    }

    public MvpFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MvpFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MvpFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public V getMvpView() {
        return (V) this;
    }

    @Override
    public P getPresenter() {
        return presenter;
    }

    public Activity getActivity() {
        Context context = getContext();
        while (!(context instanceof Activity) && context instanceof ContextWrapper)
            context = ((ContextWrapper)context).getBaseContext();
        if (!(context instanceof Activity))
            throw new IllegalStateException("Expected an activity context, got "
                    + context.getClass().getSimpleName());
        return (Activity)context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mvpDelegate.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mvpDelegate.onDetachedFromWindow();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mvpDelegate.onWindowVisibilityChanges(visibility);
    }
}
