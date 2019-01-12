package com.alapshin.arctor.sample.bar.view;

import android.support.annotation.LayoutRes;

import com.alapshin.arctor.sample.R;
import com.alapshin.arctor.sample.bar.presenter.BarPresenter;
import com.alapshin.mvp.view.BaseFragment;

import dagger.android.support.AndroidSupportInjection;

public class BarFragment extends BaseFragment<BarView, BarPresenter> implements BarView {
    @LayoutRes
    @Override
    protected int getLayoutRes() {
        return R.layout.bar_fragment;
    }

    @Override
    protected void injectDependencies() {
        AndroidSupportInjection.inject(this);
    }
}
