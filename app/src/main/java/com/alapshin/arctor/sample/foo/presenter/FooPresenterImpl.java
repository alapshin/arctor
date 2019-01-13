package com.alapshin.arctor.sample.foo.presenter;

import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.presenter.rxjava2.RxPresenter;
import com.alapshin.arctor.sample.foo.view.FooView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FooPresenterImpl extends RxPresenter<FooView> implements FooPresenter {
    @Inject
    public FooPresenterImpl() {
    }

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            Disposable dataSubscription = Observable.interval(5, 1, TimeUnit.SECONDS)
                    .compose(waitViewLatest())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> getView().setData(data));
            addDisposable(dataSubscription);

            Disposable progressSubscription = Observable.just(true)
                    .compose(waitViewLatest())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> getView().showProgress());
            addDisposable(progressSubscription);
        }
    }
}
