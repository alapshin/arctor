package com.alapshin.arctor.sample.foo.presenter;

import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.presenter.rx.RxPresenter;
import com.alapshin.arctor.sample.foo.view.FooView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FooPresenterImpl extends RxPresenter<FooView> implements FooPresenter {
    @Inject
    public FooPresenterImpl() {
    }

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            Subscription dataSubscription = Observable.interval(5, 1, TimeUnit.SECONDS)
                    .compose(deliverLatest())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> getView().setData(data));
            addSubscription(dataSubscription);

            Subscription progressSubscription = Observable.just(null)
                    .compose(deliverLatest())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> getView().showProgress());
            addSubscription(progressSubscription);
        }
    }
}
