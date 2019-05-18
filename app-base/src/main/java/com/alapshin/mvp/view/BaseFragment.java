package com.alapshin.mvp.view;

import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpFragment;
import com.alapshin.arctor.view.MvpView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<V extends MvpView, P extends Presenter<V>>
        extends MvpFragment<V, P> {
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
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @LayoutRes
    protected abstract int getLayoutRes();
    protected abstract void injectDependencies();

}

