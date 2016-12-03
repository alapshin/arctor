package com.alapshin.arctor.presenter.rx.delivery;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Transformer which couples data Observable with view Observable.
 */
public class DeliverFirst<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> view;

    public DeliverFirst(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return Observable
                .combineLatest(
                        view,
                        // Emit only first value from data Observable
                        observable.first()
                                // Use materialize to propagate onError events from data Observable
                                // only after view Observable emits true
                                .materialize()
                                .delay(new Func1<Notification<T>, Observable<Boolean>>() {
                                    @Override
                                    public Observable<Boolean> call(Notification<T> notification) {
                                        return view.filter(new Func1<Boolean, Boolean>() {
                                            @Override
                                            public Boolean call(Boolean value) {
                                                return value;
                                            }
                                        });
                                    }
                                }),
                        new Func2<Boolean, Notification<T>, Notification<T>>() {
                            @Override
                            public Notification<T> call(Boolean flag, Notification<T> notification) {
                                return flag ? notification : null;
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
