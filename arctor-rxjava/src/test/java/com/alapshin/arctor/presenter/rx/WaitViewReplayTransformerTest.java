package com.alapshin.arctor.presenter.rx;


import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class WaitViewReplayTransformerTest {
    private static final int EMIT_DELAY_IN_SECONDS = 60;
    private static final int WAIT_DELAY_IN_MILLISECONDS = 60;

    @Test
    public void shouldEmitValueWhenViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(view);
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
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(view);
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
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(view);
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
        WaitViewReplayTransformer<Integer> transformer =
                new WaitViewReplayTransformer<>(view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));
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
    public void shouldReplayAllValuesAfterViewIsAttached() {
        TestScheduler testScheduler = Schedulers.test();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewReplayTransformer<Integer> transformer =
                new WaitViewReplayTransformer<>(view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1, 2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldEmitErrorWhenViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create(true);
        WaitViewReplayTransformer<Object> transformer = new WaitViewReplayTransformer<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }

    @Test
    public void shouldNotEmitErrorWhenViewIsDetached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewReplayTransformer<Object> transformer = new WaitViewReplayTransformer<>(view);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(WAIT_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldEmitErrorAfterViewIsAttached() {
        TestScheduler testScheduler = Schedulers.test();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewReplayTransformer<Object> transformer = new WaitViewReplayTransformer<>(
                view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        Observable.error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }

    @Test
    public void shouldReplayAllValuesFromEndlessAfterViewIsAttached() {
        PublishSubject<Integer> data = PublishSubject.create();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(view);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        data.compose(transformer)
                .subscribe(testSubscriber);
        data.onNext(0);
        data.onNext(1);
        view.onNext(true);
        testSubscriber.assertValues(0, 1);
        testSubscriber.assertNotCompleted();
    }
}
