package com.alapshin.arctor.presenter.rxjava2;


import android.support.annotation.CallSuper;

import com.alapshin.arctor.presenter.BasePresenter;
import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.view.MvpView;

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

    public <T> WaitViewLatestTransformer<T> waitViewLatest() {
        return new WaitViewLatestTransformer<>(viewSubject);
    }

    public <T> WaitViewReplayTransformer<T> waitViewReplay() {
        return new WaitViewReplayTransformer<>(viewSubject);
    }
}
