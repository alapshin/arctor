package com.alapshin.arctor.presenter.rxjava2;

import android.support.annotation.CallSuper;

import com.alapshin.arctor.presenter.BasePresenter;
import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.view.MvpView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public class RxPresenter<V extends MvpView> extends BasePresenter<V> {
    CompositeDisposable disposables = new CompositeDisposable();
    BehaviorSubject<Boolean> viewSubject = BehaviorSubject.create();

    @Override
    public void onCreate(PresenterBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (viewSubject.hasComplete()) {
            viewSubject = BehaviorSubject.create();
        }
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        viewSubject.onComplete();
        disposables.clear();
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

    public void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    public void removeDisposable(Disposable disposable) {
        disposables.remove(disposable);
    }

    public void clearDisposables() {
        disposables.clear();
    }

    /**
     * Returns an {@link io.reactivex.Observable} that emits current status of a view.
     *
     * @return an {@link io.reactivex.Observable} that emits true when view attached and false when view detached.
     */
    public Observable<Boolean> viewStatus() {
        // Return view status as observable.
        // This mechanism allows us to ensure that `viewSubject` is always subscribed
        // on the main thread.
        // See https://github.com/trello/RxLifecycle/pull/105 for similar discussion
        return Observable.defer(() -> viewSubject.subscribeOn(AndroidSchedulers.mainThread()));
    }

    /**
     * Returns an {@link io.reactivex.ObservableTransformer} that delays emission from the source
     * {@link Observable}.
     *
     * Delivers latest onNext value that has been emitted by the source observable.
     *
     * @param <T> the type of source observable emissions
     */
    public <T> WaitViewLatestTransformer<T> waitViewLatest() {
        return new WaitViewLatestTransformer<>(viewStatus());
    }

    /**
     * Returns an {@link io.reactivex.ObservableTransformer} that delays emission from the source
     * {@link Observable}.
     *
     * Keeps all onNext values and emits them each time a view gets attached.
     *
     * @param <T> the type of source observable emissions
     */
    public <T> WaitViewReplayTransformer<T> waitViewReplay() {
        return new WaitViewReplayTransformer<>(viewStatus());
    }
}
