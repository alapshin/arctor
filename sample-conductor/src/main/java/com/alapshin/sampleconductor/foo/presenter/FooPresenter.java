package com.alapshin.sampleconductor.foo.presenter;


import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.sampleconductor.foo.view.FooView;

public interface FooPresenter extends Presenter<FooView> {
    void getData();
}
