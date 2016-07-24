package com.alapshin.sampleconductor.foo.view;


import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alapshin.mvp.view.BaseController;
import com.alapshin.sampleconductor.R;
import com.alapshin.sampleconductor.di.HasComponent;
import com.alapshin.sampleconductor.di.components.ActivityComponent;
import com.alapshin.sampleconductor.foo.presenter.FooPresenter;

import butterknife.BindView;

public class FooController extends BaseController<FooView, FooPresenter> implements FooView {
    @BindView(R.id.foo_textview) TextView textView;
    @BindView(R.id.foo_progressbar) ProgressBar progressBar;

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(long data) {
        progressBar.setVisibility(View.GONE);
        textView.setText(String.valueOf(data));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.foo_controller;
    }

    @Override
    protected void injectDependencies() {
        ((HasComponent<ActivityComponent>) getActivity()).component().inject(this);
    }

}
