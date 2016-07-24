package com.alapshin.arctor.presenter.rx;


import com.alapshin.arctor.presenter.rx.delivery.DeliverReplay;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;

public class DeliverReplayTest {
    private static final int DELAY_IN_MILLISECONDS = 200;

    @Test
    public void emitWithAttachedView() {
        Observable<Boolean> view = Observable.just(true);
        DeliverReplay<Integer> transformer = new DeliverReplay<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
        testSubscriber.assertCompleted();
    }

    @Test
    public void emitLastWithAttachedView() {
        Observable<Boolean> view = Observable.just(true);
        DeliverReplay<Integer> transformer = new DeliverReplay<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1, 2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void doesNotEmitWithDetachedView() {
        Observable<Boolean> view = Observable.just(false);
        DeliverReplay<Integer> transformer = new DeliverReplay<>(view);
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
        DeliverReplay<Integer> transformer = new DeliverReplay<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoValues();
    }

    @Test
    public void emitAllAfterViewAttachment() {
        Observable<Boolean> view = Observable.just(true)
                .delay(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        DeliverReplay<Integer> transformer = new DeliverReplay<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1, 2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void emitErrorWithAttachedView() {
        Observable<Boolean> view = Observable.just(true);
        DeliverReplay<Object> transformer = new DeliverReplay<>(view);
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
        DeliverReplay<Object> transformer = new DeliverReplay<>(view);
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
        DeliverReplay<Object> transformer = new DeliverReplay<>(view);
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
        DeliverReplay<Object> transformer = new DeliverReplay<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }
}
