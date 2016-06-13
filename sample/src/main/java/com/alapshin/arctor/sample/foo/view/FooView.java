package com.alapshin.arctor.sample.foo.view;

import com.alapshin.arctor.view.MvpView;

public interface FooView extends MvpView {
    public void showProgress();
    public void setData(long data);
}
