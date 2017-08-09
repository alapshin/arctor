package com.alapshin.mvp.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpActivity;
import com.alapshin.arctor.view.MvpView;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public abstract class BaseActivity<V extends MvpView, P extends Presenter<V>>
        extends MvpActivity<V, P> implements LifecycleProvider<ActivityEvent> {

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    public final <T> LifecycleTransformer<T> bindUntilEvent(ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject);
    }

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        setContentView(getLayoutRes());
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onContentChanged() {
        ButterKnife.bind(this);
        super.onContentChanged();
    }

    @LayoutRes
    protected abstract int getLayoutRes();
    protected abstract void injectDependencies();
}
