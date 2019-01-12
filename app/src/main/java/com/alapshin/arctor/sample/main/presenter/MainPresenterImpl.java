package com.alapshin.arctor.sample.main.presenter;

import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.presenter.rx.RxPresenter;
import com.alapshin.arctor.sample.main.view.MainView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenterImpl extends RxPresenter<MainView> implements MainPresenter {
    @Inject
    public MainPresenterImpl() {
    }

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            Subscription subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(deliverLatest())
                    .subscribe(data -> getView().setData(data));
            addSubscription(subscription);
        }
    }
}
