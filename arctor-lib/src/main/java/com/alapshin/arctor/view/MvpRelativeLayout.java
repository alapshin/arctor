package com.alapshin.arctor.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.alapshin.arctor.delegate.ViewGroupMvpDelegate;
import com.alapshin.arctor.delegate.ViewGroupMvpDelegateImpl;
import com.alapshin.arctor.presenter.Presenter;

import javax.inject.Inject;

public abstract class MvpRelativeLayout<V extends MvpView, P extends Presenter<V>>
        extends RelativeLayout implements MvpView {
    private static final String PARENT_STATE_KEY = "parent_state";

    @Inject
    protected P presenter;
    private ViewGroupMvpDelegate<V, P> mvpDelegate = new ViewGroupMvpDelegateImpl<>();

    public MvpRelativeLayout(Context context) {
        super(context);
    }

    public MvpRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MvpRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MvpRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        mvpDelegate.onAttachedToWindow((V) this, presenter);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
//        mvpDelegate.onSaveInstanceState(bundle);
        bundle.putParcelable(PARENT_STATE_KEY, super.onSaveInstanceState());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

        Bundle bundle = (Bundle) state;
//        mvpDelegate.onCreate(presenter, bundle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mvpDelegate.onDetachedFromWindow();
    }
}