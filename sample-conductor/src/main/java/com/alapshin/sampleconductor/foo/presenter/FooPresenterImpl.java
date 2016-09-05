package com.alapshin.sampleconductor.foo.presenter;

import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.presenter.rx.RxPresenter;
import com.alapshin.arctor.viewstate.ViewStateCommand;
import com.alapshin.sampleconductor.foo.view.FooView;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FooPresenterImpl extends RxPresenter<FooView> implements FooPresenter {
    static class DataCommand implements ViewStateCommand<FooView> {
        private final long data;

        DataCommand(long data) {
            this.data = data;
        }

        @Override
        public int type() {
            return 0;
        }

        @Override
        public void execute(FooView view) {
            view.setData(data);
        }
    }

    static class ProgressCommand implements ViewStateCommand<FooView> {
        @Override
        public int type() {
            return 1;
        }

        @Override
        public void execute(FooView view) {
            view.showProgress();
        }
    }

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            getData();
        }
    }

    @Override
    public void getData() {
        Subscription dataSubscription = Observable.interval(5, 1, TimeUnit.SECONDS)
                .compose(deliverLatest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> getView().setData(data));
        addSubscription(dataSubscription);

        ProgressCommand command = new ProgressCommand();

        Subscription progressSubscription = Observable.just(null)
                .compose(deliverLatest())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    executeCommand(command, CommandStoreStrategy.ADD);
                });
        addSubscription(progressSubscription);
    }
}