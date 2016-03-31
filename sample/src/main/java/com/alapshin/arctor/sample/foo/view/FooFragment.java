package com.alapshin.arctor.sample.foo.view;

import android.support.annotation.LayoutRes;
import android.util.Log;
import android.widget.TextView;

import com.alapshin.arctor.sample.R;
import com.alapshin.arctor.sample.di.HasComponent;
import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.arctor.sample.foo.presenter.FooPresenter;
import com.alapshin.mvp.view.BaseFragment;

import butterknife.Bind;

public class FooFragment extends BaseFragment<FooView, FooPresenter>
        implements FooView {

    private static final String TAG = FooFragment.class.getSimpleName();

    @Bind(R.id.fragment_foo_textview) TextView textView;

    @Override
    public void setData(long data) {
        Log.d(TAG, "setData " + data);
        textView.setText(String.format(getString(R.string.all_data), data));
    }

    @LayoutRes
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_foo;
    }

    @Override
    protected void injectDependencies() {
        ((HasComponent<ActivityComponent>) getActivity()).component().inject(this);
    }
}
