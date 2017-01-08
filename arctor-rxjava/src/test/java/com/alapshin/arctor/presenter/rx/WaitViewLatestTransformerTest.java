package com.alapshin.arctor.presenter.rx;


import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.BehaviorSubject;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitViewLatestTransformerTest {
    private static final int EMIT_DELAY_IN_SECONDS = 60;
    private static final int WAIT_DELAY_IN_MILLISECONDS = 60;

    @Test
    public void shouldEmitValueWhenViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create(true);
        WaitViewLatestTransformer<Integer> transformer = new WaitViewLatestTransformer<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldEmitAllValuesWhenViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create(true);
        WaitViewLatestTransformer<Integer> transformer = new WaitViewLatestTransformer<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1, 2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldNotEmitValueWhenViewIsDetached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewLatestTransformer<Integer> transformer = new WaitViewLatestTransformer<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(WAIT_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoValues();
    }

    @Test
    public void shouldEmitValueAfterViewIsAttached() {
        TestScheduler testScheduler = Schedulers.test();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<>(view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldEmitLatestValueAfterViewIsAttached() {
        final TestScheduler dataScheduler = Schedulers.test();
        final TestScheduler viewScheduler = Schedulers.test();
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewLatestTransformer<Integer> transformer = new WaitViewLatestTransformer<>(
                view.delay(2 * EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, viewScheduler));
        Observable.just(0, 1, 2)
                .concatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer value) {
                        return Observable.just(value).
                                delay(value * EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, dataScheduler);
                    }
                })
                .compose(transformer)
                .subscribe(testSubscriber);

        dataScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertNoValues();
        dataScheduler.advanceTimeBy(2 * EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        viewScheduler.advanceTimeBy(2 * EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldEmitErrorWhenViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create(true);
        WaitViewLatestTransformer<Object> transformer = new WaitViewLatestTransformer<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }

    @Test
    public void shouldNotEmitErrorWhenViewIsDetached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create(false);
        WaitViewLatestTransformer<Object> transformer = new WaitViewLatestTransformer<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(WAIT_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldEmitErrorAfterViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create(true);
        WaitViewLatestTransformer<Object> transformer = new WaitViewLatestTransformer<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }

    @Test
    public void shouldEmitValueFromEndlessAfterViewIsAttached() {
        TestScheduler testScheduler = Schedulers.test();
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();

        Observable<Boolean> view = Observable.just(true)
                .delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler);
        WaitViewLatestTransformer<Object> transformer = new WaitViewLatestTransformer<>(view);
        Observable.interval(0, 100, TimeUnit.SECONDS, testScheduler)
                .compose(transformer)
                .subscribe(testSubscriber);
        testScheduler.advanceTimeBy(500, TimeUnit.SECONDS);
        testSubscriber.assertNotCompleted();
        assertThat(testSubscriber.getOnNextEvents()).isNotEmpty();
    }

    @Test
    public void shouldEmitLatestValueFromEndlessWithSingleEmissionAfterViewIsAttached() {
        TestScheduler testScheduler = Schedulers.test();
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();

        Observable<Boolean> view = Observable.just(true)
                .delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler);
        WaitViewLatestTransformer<Object> transformer = new WaitViewLatestTransformer<>(view);

        BehaviorSubject<Integer> subject = BehaviorSubject.create(1);
        subject
                .compose(transformer)
                .subscribe(testSubscriber);
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertValue(1);
        testSubscriber.assertNotCompleted();
    }
}
