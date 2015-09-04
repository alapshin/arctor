package com.alapshin.arctor.presenter.rx.delivery;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subjects.ReplaySubject;

public class DeliverReplay<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> semaphore;

    public DeliverReplay(Observable<Boolean> semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        final ReplaySubject<T> subject = ReplaySubject.create();
        final Subscription subscription = observable.subscribe(subject);
        return semaphore
                .switchMap(new Func1<Boolean, Observable<T>>() {
                    @Override
                    public Observable<T> call(final Boolean flag) {
                        return flag.booleanValue() ? subject : Observable.<T>never();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        subscription.unsubscribe();
                    }
                });
    }
}
