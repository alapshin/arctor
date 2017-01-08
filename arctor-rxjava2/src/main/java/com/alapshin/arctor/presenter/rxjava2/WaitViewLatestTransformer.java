package com.alapshin.arctor.presenter.rxjava2;


import com.alapshin.arctor.presenter.rxjava2.util.Optional;


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
 * If view is attached (latest emitted value from view observable is true) then values from upstream observable
 * propagate as usual.
 *
 * If view is detached (latest emitted value from view observable is false) then values from upstream
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
        return Observable
                .combineLatest(
                        view,
                        upstream
                                // Materialize upstream Observable to handle onError and onComplete events when view is detached
                                .materialize()
                                // If this is onNext notification then emit it immediately
                                // If this is onComplete notification then delay emission of this notification while view is detached
                                .delay(new Function<Notification<T>, Observable<Boolean>>() {
                                    @Override
                                    public Observable<Boolean> apply(Notification<T> notification) {
                                        if (!notification.isOnComplete()) {
                                            return Observable.just(true);
                                        } else {
                                            return view.filter(new Predicate<Boolean>() {
                                                @Override
                                                public boolean test(Boolean value) throws Exception {
                                                    return value;
                                                }
                                            });
                                        }
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
