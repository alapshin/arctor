package com.alapshin.arctor.presenter.rx.delivery;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Transformer that couples data and view status.
 *
 * Emits latest value from source observable and never terminates.
 *
 */
public class DeliverLatestCache<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> view;

    public DeliverLatestCache(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return Observable
                .combineLatest(
                        view,
                        observable
                                .materialize()
                                .filter(new Func1<Notification<T>, Boolean>() {
                                    @Override
                                    public Boolean call(Notification<T> notification) {
                                        return !notification.isOnCompleted();
                                    }
                                }),
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
