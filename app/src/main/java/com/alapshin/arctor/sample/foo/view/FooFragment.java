package com.alapshin.arctor.sample.foo.view;

import androidx.annotation.LayoutRes;
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
    public void onProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onData(long data) {
        Log.d(TAG, "onData " + data);
        textView.setText(String.format(getString(R.string.all_data), data));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(Throwable error) {
    }

    @Override
    @LayoutRes
    protected int getLayoutRes() {
        return R.layout.foo_fragment;
    }

    @Override
    protected void injectDependencies() {
        AndroidSupportInjection.inject(this);
    }
}
