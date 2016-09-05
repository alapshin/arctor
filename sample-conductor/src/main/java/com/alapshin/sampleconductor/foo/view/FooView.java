package com.alapshin.sampleconductor.foo.view;


import com.alapshin.arctor.view.MvpView;

public interface FooView extends MvpView {
    void showProgress();
    void setData(long data);
}
