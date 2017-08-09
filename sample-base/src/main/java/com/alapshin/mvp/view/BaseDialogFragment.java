package com.alapshin.mvp.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpDialogFragment;
import com.alapshin.arctor.view.MvpView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialogFragment<V extends MvpView, P extends Presenter<V>>
        extends MvpDialogFragment<V, P> {
    private Unbinder unbinder;

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @LayoutRes
    protected abstract int getLayoutRes();
    protected abstract void injectDependencies();

}


