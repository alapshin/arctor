package com.alapshin.arctor.presenter.rx;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class WaitViewReplayTransformerTest {
    private static final int DELAY_IN_SECONDS = 60;

    private BehaviorSubject<Boolean> view;
    private TestScheduler scheduler;
    private WaitViewReplayTransformer<Integer> transformer;
    private TestSubscriber<Integer> testSubscriber;

    @Before
    public void setup() {
        view = BehaviorSubject.create();
        scheduler = new TestScheduler();
        testSubscriber = new TestSubscriber<>();
        transformer = new WaitViewReplayTransformer<>(view);
    }

    @Test
    public void shouldEmitValueWhenViewIsAttached() {
        view.onNext(true);
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldEmitAllValuesWhenViewIsAttached() {
        view.onNext(true);
        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1, 2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldNotEmitValueWhenViewIsDetached() {
        Observable.just(0)
                .subscribeOn(scheduler)
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertNoValues();
    }

    @Test
    public void shouldEmitValueAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewReplayTransformer<>(view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldReplayAllValuesAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewReplayTransformer<>(view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1, 2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldEmitErrorWhenViewIsAttached() {
        view.onNext(true);
        Observable.<Integer>error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }

    @Test
    public void shouldNotEmitErrorWhenViewIsDetached() {
        Observable.<Integer>error(new RuntimeException())
                .subscribeOn(scheduler)
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldEmitErrorAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewReplayTransformer<>(
                view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));
        Observable.<Integer>error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
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
