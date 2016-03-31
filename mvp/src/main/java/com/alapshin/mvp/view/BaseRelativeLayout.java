package com.alapshin.mvp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpRelativeLayout;
import com.alapshin.arctor.view.MvpView;

public abstract class BaseRelativeLayout<V extends MvpView, P extends Presenter<V>> extends MvpRelativeLayout {
    public BaseRelativeLayout(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        injectDependencies();
        LayoutInflater.from(context).inflate(getLayoutRes(), this);
    }

    @LayoutRes
    protected abstract int getLayoutRes();
    protected abstract void injectDependencies();
}
