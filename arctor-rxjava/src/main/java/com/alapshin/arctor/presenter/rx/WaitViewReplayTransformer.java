package com.alapshin.arctor.presenter.rx;

import rx.Notification;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subjects.ReplaySubject;

public class WaitViewReplayTransformer<T> implements Observable.Transformer<T, T> {
    private final Observable<Boolean> view;

    public WaitViewReplayTransformer(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(final Observable<T> observable) {
        final ReplaySubject<Notification<T>>
                subject = ReplaySubject.create();
        final Subscription subscription = observable.materialize().subscribe(subject);
        return view
                .switchMap(new Func1<Boolean, Observable<Notification<T>>>() {
                    @Override
                    public Observable<Notification<T>> call(final Boolean flag) {
                        if (flag) {
                            return subject;
                        } else {
                            return Observable.empty();
                        }
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        subscription.unsubscribe();
                    }
                })
                .dematerialize();
    }
}
