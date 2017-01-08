package com.alapshin.arctor.presenter.rx;


import android.support.annotation.CallSuper;

import com.alapshin.arctor.presenter.BasePresenter;
import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.view.MvpView;

import javax.annotation.Nullable;

import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author alapshin
 * @since 2015-07-06
 */
public class RxPresenter<V extends MvpView> extends BasePresenter<V> {
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private BehaviorSubject<Boolean> viewSubject = BehaviorSubject.create();

    /**
     * Returns an {@link rx.Observable} that emits current status of a view.
     *
     * @return an {@link rx.Observable} that emits true when view attached and false when view detached.
     */
    public Observable<Boolean> viewStatus() {
        return viewSubject;
    }

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        super.onCreate(bundle);
        if (viewSubject.hasCompleted()) {
            viewSubject = BehaviorSubject.create();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();

        viewSubject.onCompleted();
        subscriptions.clear();
    }

    @Override
    @CallSuper
    public void attachView(V view) {
        super.attachView(view);
        viewSubject.onNext(true);
    }

    @Override
    @CallSuper
    public void detachView() {
        super.detachView();
        viewSubject.onNext(false);
    }

    /**
     * Registers a subscription to automatically unsubscribe it during onDestroy.
     * See {@link CompositeSubscription#add(Subscription) for details.}
     *
     * @param subscription a subscription to add.
     */
    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    /**
     * Removes and unsubscribes a subscription that has been registered with
     * {@link #addSubscription(Subscription)} previously.
     * See {@link CompositeSubscription#remove(Subscription) for details.}
     *
     * @param subscription a subscription to remove.
     */
    public void removeSubscription(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    /**
     * Removes and unsubscribes from all subscriptions that has been registered with
     * {@link #addSubscription(Subscription)} previously.
     *
     * See {@link CompositeSubscription#clear() for details.}
     */
    public void clearSubscriptions() {
        subscriptions.clear();
    }

    /**
     * Returns an {@link rx.Observable.Transformer} that delays emission from the source
     * {@link rx.Observable}.
     *
     * Delivers latest onNext value that has been emitted by the source observable.
     *
     * @param <T> the type of source observable emissions
     */
    public <T> WaitViewLatestTransformer<T> deliverLatest() {
        return new WaitViewLatestTransformer<>(viewStatus());
    }

    /**
     * Returns an {@link rx.Observable.Transformer} that delays emission from the source
     * {@link rx.Observable}.
     *
     * Keeps all onNext values and emits them each time a new view gets attached.
     *
     * @param <T> the type of source observable emissions
     */
    public <T> WaitViewReplayTransformer<T> deliverReplay() {
        return new WaitViewReplayTransformer<>(viewStatus());
    }
}
