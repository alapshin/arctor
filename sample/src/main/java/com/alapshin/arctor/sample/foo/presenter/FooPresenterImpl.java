package com.alapshin.arctor.sample.foo.presenter;

import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.presenter.rx.RxPresenter;
import com.alapshin.arctor.sample.foo.view.FooView;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FooPresenterImpl extends RxPresenter<FooView> implements FooPresenter {
    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            Subscription dataSubscription = Observable.interval(10, 1, TimeUnit.SECONDS)
                    .compose(deliverLatestCache())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> getView().setData(data));
            addSubscription(dataSubscription);

            Subscription progressSubscription = Observable.just(null)
                    .compose(deliverLatestCache())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> getView().showProgress());
            addSubscription(progressSubscription);
        }
    }
}
