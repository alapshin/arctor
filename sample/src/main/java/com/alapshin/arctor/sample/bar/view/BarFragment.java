package com.alapshin.arctor.sample.bar.view;

import android.support.annotation.LayoutRes;

import com.alapshin.arctor.sample.R;
import com.alapshin.arctor.sample.bar.presenter.BarPresenter;
import com.alapshin.arctor.sample.di.HasComponent;
import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.mvp.view.CustomFragment;

public class BarFragment extends CustomFragment<BarView, BarPresenter> implements BarView {
    @LayoutRes
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bar;
    }

    @Override
    protected void injectDependencies() {
        ((HasComponent<ActivityComponent>) getActivity()).component().inject(this);
    }
}
