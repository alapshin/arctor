package com.alapshin.mvp.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpActivity;
import com.alapshin.arctor.view.MvpView;
import com.alapshin.di.ComponentCache;
import com.alapshin.di.ComponentCacheDelegate;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public abstract class BaseActivity<V extends MvpView, P extends Presenter<V>>
        extends MvpActivity<V, P> implements LifecycleProvider<ActivityEvent>, ComponentCache {

    private final ComponentCacheDelegate componentCacheDelegate = new ComponentCacheDelegate();
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
        // {@link ComponentCacheDelegate#onCreate} should be called before {@link Activity#onCreate}
        componentCacheDelegate.onCreate(savedInstanceState, getLastCustomNonConfigurationInstance());

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
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    @Override
    @CallSuper
    public Object onRetainCustomNonConfigurationInstance() {
        return componentCacheDelegate.onRetainCustomNonConfigurationInstance();
    }

    @Override
    public long generateComponentId() {
        return componentCacheDelegate.generateId();
    }

    @Override
    public <C> C getComponent(long index) {
        return componentCacheDelegate.getComponent(index);
    }

    @Override
    public <C> void setComponent(long index, C component) {
        componentCacheDelegate.setComponent(0, component);
    }

    @LayoutRes
    protected abstract int getLayoutRes();
    protected abstract void injectDependencies();
}
