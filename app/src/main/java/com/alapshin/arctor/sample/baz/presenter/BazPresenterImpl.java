package com.alapshin.arctor.sample.baz.presenter;

import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.presenter.rx.RxPresenter;
import com.alapshin.arctor.sample.baz.view.BazView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BazPresenterImpl extends RxPresenter<BazView> implements BazPresenter {
    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        super.onCreate(bundle);
        Subscription subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(deliverLatest())
                .subscribe(data -> getView().setData(data));
        addSubscription(subscription);
    }
}
