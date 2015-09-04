package com.alapshin.arctor.presenter.rx.delivery;

import android.util.Log;

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
                });
    }
}