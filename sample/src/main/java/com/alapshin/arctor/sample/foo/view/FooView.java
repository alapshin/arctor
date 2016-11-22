package com.alapshin.arctor.sample.foo.view;

import com.alapshin.arctor.annotation.GenerateCommand;
import com.alapshin.arctor.view.MvpView;

public interface FooView extends MvpView {
    void showProgress();
    @GenerateCommand
    void setData(long data);
}
