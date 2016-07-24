package com.alapshin.arctor.presenter.rx;


import com.alapshin.arctor.presenter.rx.delivery.DeliverFirst;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;

public class DeliverFirstTest {
    private static final int DELAY_IN_MILLISECONDS = 200;

    @Test
    public void emitWithAttachedView() {
        Observable<Boolean> view = Observable.just(true);
        DeliverFirst<Integer> transformer = new DeliverFirst<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
    }

    @Test
    public void emitFirstWithAttachedView() {
        Observable<Boolean> view = Observable.just(true);
        DeliverFirst<Integer> transformer = new DeliverFirst<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
    }

    @Test
    public void doesNotEmitWithDetachedView() {
        Observable<Boolean> view = Observable.just(false);
        DeliverFirst<Integer> transformer = new DeliverFirst<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoValues();
    }

    @Test
    public void doesNotEmitWithNeverAttachedView() {
        Observable<Boolean> view = Observable.never();
        DeliverFirst<Integer> transformer = new DeliverFirst<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoValues();
    }

    @Test
    public void emitFirstAfterViewAttachment() {
        Observable<Boolean> view = Observable.just(true)
                .delay(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        DeliverFirst<Integer> transformer = new DeliverFirst<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
    }

    @Test
    public void emitErrorWithAttachedView() {
        Observable<Boolean> view = Observable.just(true);
        DeliverFirst<Object> transformer = new DeliverFirst<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }

    @Test
    public void doesNotEmitErrorWithDetachedView() {
        Observable<Boolean> view = Observable.just(false);
        DeliverFirst<Object> transformer = new DeliverFirst<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void doesNotEmitErrorWithNeverAttachedView() {
        Observable<Boolean> view = Observable.never();
        DeliverFirst<Object> transformer = new DeliverFirst<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void emitErrorAfterViewAttachment() {
        Observable<Boolean> view = Observable.just(true)
                .delay(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        DeliverFirst<Object> transformer = new DeliverFirst<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }
}
