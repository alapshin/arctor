package com.alapshin.arctor.presenter;

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
    private BehaviorSubject<Boolean> viewStatusSubject = BehaviorSubject.create();

    /**
     * Returns an observable that emits current status of a view.
     * True - a view is attached, False - a view is detached.
     *
     * @return an observable that emits current status of a view.
     */
    public Observable<Boolean> viewStatus() {
        return viewStatusSubject;
    }

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        super.onCreate(bundle);
        viewStatusSubject.onNext(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        subscriptions.unsubscribe();
        viewStatusSubject.onCompleted();
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        viewStatusSubject.onNext(true);
    }

    @Override
    public void detachView() {
        super.detachView();
        viewStatusSubject.onNext(false);
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
     * Removes and unsubscribes a subscription that has been registered with {@link #addSubscription(Subscription)} previously.
     * See {@link CompositeSubscription#remove(Subscription) for details.}
     *
     * @param subscription a subscription to remove.
     */
    public void removeSubscription(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    /**
     * Removes and unsubscribes from all subscripions that has been registered with {@link #addSubscription(Subscription)} previously.
     * Can be called from {@link Presenter#onStop()} if needed
     */
    public void clearSubscriptions() {
        subscriptions.clear();
    }


    /**
     * Returns a transformer that will delay onNext, onError and onComplete emissions unless a view become available.
     * getView() is guaranteed to be != null during all emissions. This transformer can only be used on application's main thread.
     * <p/>
     * Use this operator if you need to deliver *all* emissions to a view, in example when you're sending items
     * into adapter one by one.
     *
     * @param <T> a type of onNext value.
     * @return the delaying operator.
     */
    public <T> Observable.Transformer<T, T> deliver() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.lift(OperatorSemaphore.<T>semaphore(viewStatus()));
            }
        };
    }

    /**
     * Returns a transformer that will delay onNext, onError and onComplete emissions unless a view become available.
     * getView() is guaranteed to be != null during all emissions. This transformer can only be used on application's main thread.
     * <p/>
     * If this transformer receives a next value while the previous value has not been delivered, the
     * previous value will be dropped.
     * <p/>
     * Use this operator when you need to show updatable data.
     *
     * @param <T> a type of onNext value.
     * @return the delaying operator.
     */
    public <T> Observable.Transformer<T, T> deliverLatest() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.lift(OperatorSemaphore.<T>semaphoreLatest(viewStatus()));
            }
        };
    }

    /**
     * Returns a transformer that will delay onNext, onError and onComplete emissions unless a view become available.
     * getView() is guaranteed to be != null during all emissions. This transformer can only be used on application's main thread.
     * <p/>
     * If the transformer receives a next value while the previous value has not been delivered, the
     * previous value will be dropped.
     * <p/>
     * The transformer will duplicate the latest onNext emission in case if a view has been reattached.
     * <p/>
     * This operator ignores onComplete emission and never sends one.
     * <p/>
     * Use this operator when you need to show updatable data that needs to be cached in memory.
     *
     * @param <T> a type of onNext value.
     * @return the delaying operator.
     */
    public <T> Observable.Transformer<T, T> deliverLatestCache() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.lift(OperatorSemaphore.<T>semaphoreLatestCache(viewStatus()));
            }
        };
    }
}
