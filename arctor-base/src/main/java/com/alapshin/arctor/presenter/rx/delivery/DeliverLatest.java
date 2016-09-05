package com.alapshin.arctor.presenter.rx.delivery;

import java.util.List;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * {@link rx.Observable.Transformer} that couples data and view status
 *
 * If view is attached (latest emitted value from view observable is true) then values from data
 * observable propagates us usual.
 *
 * If view is detached (latest emitted value from view observable is false) then values from data
 * observable propagates using following rules:
 * <ul>
 *     <li>If data observable emits onError then it would be delivered after view is attached</li>
 *     <li>If data observable emits onCompleted then after view is attached last onNext value from
 *     data observable is delivered followed by onCompleted event</li>
 *     <li>If data observable emits multiple values then after view is attached last emitted value
 *     is delivered</li>
 * </ul>
 *
 */
public class DeliverLatest<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> view;

    public DeliverLatest(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return Observable
                .combineLatest(
                        view,
                        observable
                                // Materialize data Observable to handle onError and onCompleted events
                                // when view is detached
                                .materialize()
                                // Keep the last two notifications
                                .buffer(2, 1)
                                // If the last received notification is onCompleted then delay
                                // emission of previous onNext and onCompleted notification until view is attached
                                .delay(new Func1<List<Notification<T>>, Observable<Boolean>>() {
                                    @Override
                                    public Observable<Boolean> call(List<Notification<T>> notifications) {
                                        Notification<T> lastNotification =
                                                notifications.get(notifications.size() - 1);
                                        if (lastNotification.isOnCompleted()) {
                                            return view.filter(new Func1<Boolean, Boolean>() {
                                                @Override
                                                public Boolean call(Boolean value) {
                                                    return value;
                                                }
                                            });
                                        } else {
                                            return Observable.just(true);
                                        }
                                    }
                                })
                                // Remove duplicate notifications caused by sliding buffer
                                .scan(new Func2<List<Notification<T>>, List<Notification<T>>, List<Notification<T>>>() {
                                    @Override
                                    public List<Notification<T>> call(List<Notification<T>> notifications,
                                                                      List<Notification<T>> notifications2) {
                                        if (notifications == null) {
                                            // If it is first buffer of notifications emit it as usual
                                            return notifications2;
                                        } else {
                                            // Otherwise remove first element from buffer since it is
                                            // alread emitted as last element of the previous buffer
                                            return notifications2.subList(1, notifications2.size());
                                        }
                                    }
                                })
                                // Flatten emitted buffers
                                .flatMap(new Func1<List<Notification<T>>, Observable<Notification<T>>>() {
                                    @Override
                                    public Observable<Notification<T>> call(List<Notification<T>> notifications) {
                                        return Observable.from(notifications);
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
