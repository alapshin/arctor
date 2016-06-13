package com.alapshin.arctor.sample.baz.view;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.alapshin.arctor.sample.R;
import com.alapshin.arctor.sample.baz.presenter.BazPresenter;
import com.alapshin.arctor.sample.di.HasComponent;
import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.mvp.view.BaseFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BazViewGroup extends BaseFrameLayout<BazView, BazPresenter> implements BazView {

    private static final String TAG = BazViewGroup.class.getSimpleName();

    @BindView(R.id.viewgroup_baz_textview) TextView textView;

    public BazViewGroup(Context context) {
        super(context);
        ButterKnife.bind(this);
    }

    @Override
    public void setData(long data) {
        Log.d(TAG, "setData " + data);
        textView.setText(String.format(getContext().getString(R.string.all_data), data));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.viewgroup_baz;
    }

    @Override
    protected void injectDependencies() {
        ((HasComponent<ActivityComponent>) getActivity()).component().inject(this);
    }
}
