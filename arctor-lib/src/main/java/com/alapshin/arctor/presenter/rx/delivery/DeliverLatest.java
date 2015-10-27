package com.alapshin.arctor.presenter.rx.delivery;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class DeliverLatest<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> semaphore;

    public DeliverLatest(Observable<Boolean> semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return Observable
                .combineLatest(
                        semaphore,
                        observable.materialize(),
                        new Func2<Boolean, Notification<T>, Notification<T>>() {
                            @Override
                            public Notification<T> call(Boolean flag, Notification<T> notification) {
                                return flag.booleanValue() ? notification : null;
                            }
                        })
                .filter(new Func1<Notification<T>, Boolean>() {
                    @Override
                    public Boolean call(Notification<T> notification) {
                        return notification != null;
                    }
                })
                .dematerialize();
    }
}
