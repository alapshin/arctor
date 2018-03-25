package com.alapshin.arctor.presenter.rx;

import rx.Observable;

/**
 * {@link rx.Observable.Transformer} that couples data and view status
 *
 * If view is attached (latest emitted value from view observable is true) then values from source
 * observable propagates us usual.
 *
 * If view is detached (latest emitted value from view observable is false) then values from source
 * observable propagates using following rules:
 * <ul>
 *     <li>If source observable emits onError then it would be delivered after view is attached</li>
 *     <li>If source observable emits onCompleted then after view is attached last onNext value from
 *     source observable is delivered followed by onCompleted event</li>
 *     <li>If source observable emits multiple values then after view is attached most recent emitted value
 *     is delivered followed by subsequent observed items as usual</li>
 * </ul>
 *
 */
public class WaitViewLatestTransformer<T> implements Observable.Transformer<T, T> {
    private final Observable<Boolean> view;

    public WaitViewLatestTransformer(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(Observable<T> source) {
        return Observable
                .combineLatest(
                        view,
                        source
                                // Materialize source Observable to delay onError and onCompleted events while view is detached
                                .materialize()
                                // If this is onNext notification then emit it immediately
                                // If this is onComplete notification then delay emission of this notification while view is detached
                                .delay(notification -> {
                                    if (!notification.isOnCompleted()) {
                                        return Observable.just(true);
                                    } else {
                                        return view.filter(value -> value);
                                    }
                                }),
                        (flag, notification) -> flag ? notification : null)
                .filter(notification -> notification != null)
                .dematerialize();
    }
}
