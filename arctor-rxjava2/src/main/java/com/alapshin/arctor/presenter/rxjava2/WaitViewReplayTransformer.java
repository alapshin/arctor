package com.alapshin.arctor.presenter.rxjava2;


import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.ReplaySubject;

public class WaitViewReplayTransformer<T> implements ObservableTransformer<T, T> {
    private final Observable<Boolean> view;

    public WaitViewReplayTransformer(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        final ReplaySubject<Notification<T>> subject = ReplaySubject.create();
        final DisposableObserver<Notification<T>> observer = upstream.materialize()
                .subscribeWith(new DisposableObserver<Notification<T>>() {
                    @Override
                    public void onComplete() {
                        subject.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subject.onError(e);

                    }
                    @Override
                    public void onNext(Notification<T> value) {
                        subject.onNext(value);
                    }
                });

        return view
                .switchMap(new Function<Boolean, Observable<Notification<T>>>() {
                    @Override
                    public Observable<Notification<T>> apply(final Boolean flag) {
                        if (flag) {
                            return subject;
                        } else {
                            return Observable.empty();
                        }
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        observer.dispose();
                    }
                })
                .dematerialize();
    }
}
