package com.alapshin.arctor.sample.foo.view;

import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alapshin.arctor.sample.R;
import com.alapshin.arctor.sample.foo.presenter.FooPresenter;
import com.alapshin.mvp.view.BaseFragment;

import butterknife.BindView;
import dagger.android.support.AndroidSupportInjection;

public class FooFragment extends BaseFragment<FooView, FooPresenter>
        implements FooView {

    private static final String TAG = FooFragment.class.getSimpleName();

    @BindView(R.id.foo_textview) TextView textView;
    @BindView(R.id.foo_progressbar) ProgressBar progressBar;

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(long data) {
        Log.d(TAG, "setData " + data);
        textView.setText(String.format(getString(R.string.all_data), data));
        progressBar.setVisibility(View.GONE);
    }

    @LayoutRes
    @Override
    protected int getLayoutRes() {
        return R.layout.foo_fragment;
    }

    @Override
    protected void injectDependencies() {
        AndroidSupportInjection.inject(this);
    }
}
