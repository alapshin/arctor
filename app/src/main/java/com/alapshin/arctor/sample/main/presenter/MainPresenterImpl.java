package com.alapshin.arctor.sample.main.presenter;

import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.presenter.rxjava2.RxPresenter;
import com.alapshin.arctor.sample.main.view.MainView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenterImpl extends RxPresenter<MainView> implements MainPresenter {
    @Inject
    public MainPresenterImpl() {
    }

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            Disposable subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(waitViewLatest())
                    .subscribe(data -> getView().setData(data));
            addDisposable(subscription);
        }
    }
}
