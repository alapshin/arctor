package com.alapshin.arctor.presenter.rxjava2;


import com.alapshin.arctor.presenter.rxjava2.util.Optional;

import java.util.List;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * {@link ObservableTransformer} that ties upstream {@link Observable} emission  to Observable representing view status
 *
 * When view is attached (latest emitted value from view observable is true) then values from data observable propagates
 * as usual.
 *
 * When view is detached (latest emitted value from view observable is false) then values from upstream
 * Observable propagate using following rules:
 * <ul>
 *     <li>If upstream Observable emits onError then it would be delivered after view is attached</li>
 *     <li>If upstream Observable emits onComplete then after view is attached most recent onNext value from
 *     upstream Observable is delivered followed by onComplete event</li>
 *     <li>If upstream Observable emits multiple values then after view is attached most recent emitted value
 *     is delivered followed by subsequent observed items as usual</li>
 * </ul>
 *
 */
public class WaitViewLatestTransformer<T> implements ObservableTransformer<T, T> {
    private final Observable<Boolean> view;

    public WaitViewLatestTransformer(Observable<Boolean> view) {
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

        return Observable
                .combineLatest(
                        view,
                        upstream
                                // Materialize upstream Observable to handle onError and onComplete events when view is detached
                                .materialize()
                                // Create sliding buffer of size two
                                .buffer(2, 1)
                                // If the last received notification is onComplete then delay
                                // emission of previous onNext and onCompleted notification until view is attached
                                .delay(new Function<List<Notification<T>>, Observable<Boolean>>() {
                                    @Override
                                    public Observable<Boolean> apply(List<Notification<T>> notifications) {
                                        Notification<T> lastNotification =
                                                notifications.get(notifications.size() - 1);
                                        if (lastNotification.isOnComplete()) {
                                            // If upstream Observable completes then delay buffer emission until view is attached
                                            return delayObservable;
                                        } else {
                                            return Observable.just(true);
                                        }
                                    }
                                })
                                // Remove duplicate notifications caused by sliding buffer
                                .scan(new BiFunction<List<Notification<T>>, List<Notification<T>>,
                                        List<Notification<T>>>() {
                                    @Override
                                    public List<Notification<T>> apply(List<Notification<T>> notifications,
                                                                       List<Notification<T>> notifications2) {
                                        // Remove first element from buffer since it is
                                        // already emitted as last element of the previous buffer
                                        return notifications2.subList(1, notifications2.size());
                                    }
                                })
                                // Flatten emitted buffers
                                .flatMap(new Function<List<Notification<T>>, Observable<Notification<T>>>() {
                                    @Override
                                    public Observable<Notification<T>> apply(List<Notification<T>> notifications) {
                                        return Observable.fromIterable(notifications);
                                    }
                                }),
                        new BiFunction<Boolean, Notification<T>, Optional<Notification<T>>>() {
                            @Override
                            public Optional<Notification<T>> apply(Boolean flag, Notification<T> notification) {
                                return flag ? Optional.of(notification) : Optional.<Notification<T>>ofNullable(null);
                            }
                        })
                .filter(new Predicate<Optional<Notification<T>>>() {
                    @Override
                    public boolean test(Optional<Notification<T>> notification) {
                        return notification.isPresent();
                    }
                })
                .map(new Function<Optional<Notification<T>>, Notification<T>>() {
                    @Override
                    public Notification<T> apply(Optional<Notification<T>> notificationOptional) throws Exception {
                        return notificationOptional.get();
                    }
                })
                .dematerialize();
    }

}
