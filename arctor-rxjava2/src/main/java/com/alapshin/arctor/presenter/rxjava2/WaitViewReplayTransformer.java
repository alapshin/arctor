package com.alapshin.arctor.presenter.rxjava2;


import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.ReplaySubject;

public class WaitViewReplayTransformer<T> implements
        MaybeTransformer<T, T>,
        SingleTransformer<T, T>,
        CompletableTransformer,
        ObservableTransformer<T, T> {
    private final Observable<Boolean> view;

    public WaitViewReplayTransformer(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return ((Observable<T>) apply(upstream.toObservable())).singleElement();
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return ((Observable<T>) apply(upstream.toObservable())).singleOrError();
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return ((Observable<T>) apply(upstream.<T>toObservable())).ignoreElements();
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
