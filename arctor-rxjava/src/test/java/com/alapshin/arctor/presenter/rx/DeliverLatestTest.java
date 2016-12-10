package com.alapshin.arctor.presenter.rx;


import com.alapshin.arctor.presenter.rx.delivery.DeliverLatest;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static org.assertj.core.api.Assertions.assertThat;

public class DeliverLatestTest {
    private static final int DELAY_IN_MILLISECONDS = 200;

    @Test
    public void emitWithAttachedView() {
        Observable<Boolean> view = Observable.just(true);
        DeliverLatest<Integer> transformer = new DeliverLatest<>(view);
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
        DeliverLatest<Integer> transformer = new DeliverLatest<>(view);
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
        DeliverLatest<Integer> transformer = new DeliverLatest<>(view);
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
        DeliverLatest<Integer> transformer = new DeliverLatest<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoValues();
    }

    @Test
    public void emitLastAfterViewAttachment() {
        TestScheduler testScheduler = Schedulers.test();
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable<Boolean> view = Observable.just(true)
                .delay(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS, testScheduler);
        DeliverLatest<Integer> transformer = new DeliverLatest<>(view);
        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);
        testScheduler.advanceTimeBy(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void emitErrorWithAttachedView() {
        Observable<Boolean> view = Observable.just(true);
        DeliverLatest<Object> transformer = new DeliverLatest<>(view);
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
        DeliverLatest<Object> transformer = new DeliverLatest<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void doesNotEmitErrorWithNeverAttachedView() {
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable<Boolean> view = Observable.never();
        DeliverLatest<Object> transformer = new DeliverLatest<>(view);
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
        DeliverLatest<Object> transformer = new DeliverLatest<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }

    @Test
    public void emitValueFromEndlessAfterViewAttachment() {
        TestScheduler testScheduler = Schedulers.test();
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();

        Observable<Boolean> view = Observable.just(true)
                .delay(DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS, testScheduler);
        DeliverLatest<Object> transformer = new DeliverLatest<>(view);
        Observable.interval(0, 100, TimeUnit.MILLISECONDS, testScheduler)
                .compose(transformer)
                .subscribe(testSubscriber);
        testScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS);
        testSubscriber.assertNotCompleted();
        assertThat(testSubscriber.getOnNextEvents()).isNotEmpty();
    }
}
