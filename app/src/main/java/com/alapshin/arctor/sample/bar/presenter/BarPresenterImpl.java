package com.alapshin.arctor.sample.bar.presenter;

import com.alapshin.arctor.presenter.rxjava2.RxPresenter;
import com.alapshin.arctor.sample.bar.view.BarView;

import javax.inject.Inject;

public class BarPresenterImpl extends RxPresenter<BarView> implements BarPresenter {
    @Inject
    public BarPresenterImpl() {
    }
}
