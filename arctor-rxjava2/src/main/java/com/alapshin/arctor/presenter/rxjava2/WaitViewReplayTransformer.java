package com.alapshin.arctor.presenter.rxjava2;


import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class WaitViewReplayTransformer<T> implements ObservableTransformer<T, T> {
    private final Observable<Boolean> view;

    public WaitViewReplayTransformer(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        final Observable<Boolean> delayObservable = view
                .replay(1)
                .autoConnect()
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean value) throws Exception {
                        return value;
                    }
                });

        return upstream
                .materialize()
                .delay(new Function<Notification<T>, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Notification<T> notification) throws Exception {
                        return delayObservable;
                    }
                })
                .dematerialize();
    }
}
