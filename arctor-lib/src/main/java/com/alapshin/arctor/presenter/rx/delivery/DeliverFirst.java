package com.alapshin.arctor.presenter.rx.delivery;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class DeliverFirst<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> semaphore;

    public DeliverFirst(Observable<Boolean> semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return Observable
                .combineLatest(
                semaphore,
                observable,
                    new Func2<Boolean, T, T>() {
                        @Override
                        public T call(Boolean flag, T value) {
                            return flag.booleanValue() ? value : null;
                        }
                    })
                .filter(new Func1<T, Boolean>() {
                    @Override
                    public Boolean call(T value) {
                        return value != null;
                    }
            })
            .take(1);
    }
}
