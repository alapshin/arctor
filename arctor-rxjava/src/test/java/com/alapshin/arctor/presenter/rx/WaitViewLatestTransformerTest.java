package com.alapshin.arctor.presenter.rx;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.BehaviorSubject;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitViewLatestTransformerTest {
    private static final int DELAY_IN_SECONDS = 60;

    private BehaviorSubject<Boolean> view;
    private TestScheduler scheduler;
    private WaitViewLatestTransformer<Integer> transformer;
    private TestSubscriber<Integer> testSubscriber;

    @Before
    public void setup() {
        view = BehaviorSubject.create();
        scheduler = new TestScheduler();
        testSubscriber = new TestSubscriber<>();
        transformer = new WaitViewLatestTransformer<>(view);
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
                .compose(transformer)
                .subscribeOn(scheduler)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertNoValues();
    }

    @Test
    public void shouldEmitValueAfterViewIsAttached() {
        view.onNext(true);
        transformer =
                new WaitViewLatestTransformer<>(view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));
        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldNotEmitValuesAfterViewIsDetached() {
        view.onNext(true);
        Observable.interval(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler)
                .map(Long::intValue)
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);
        view.onNext(false);
        scheduler.advanceTimeBy(2 * DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void shouldEmitLatestValueAfterViewIsAttached() {
        view.onNext(true);
        final TestScheduler dataScheduler = Schedulers.test();
        final TestScheduler viewScheduler = Schedulers.test();
        transformer = new WaitViewLatestTransformer<>(
                view.delay(2 * DELAY_IN_SECONDS, TimeUnit.SECONDS, viewScheduler));
        Observable.just(0, 1, 2)
                .concatMap(value -> Observable.just(value).
                        delay(value * DELAY_IN_SECONDS, TimeUnit.SECONDS, dataScheduler))
                .compose(transformer)
                .subscribe(testSubscriber);

        dataScheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertNoValues();
        dataScheduler.advanceTimeBy(2 * DELAY_IN_SECONDS, TimeUnit.SECONDS);
        viewScheduler.advanceTimeBy(2 * DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(2);
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
        Observable.<Integer>error(new RuntimeException())
                .compose(transformer)
                .subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(RuntimeException.class);
    }

    @Test
    public void shouldEmitValueFromEndlessAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewLatestTransformer<>(view
                .delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));
        Observable.interval(0, 100, TimeUnit.SECONDS, scheduler)
                .map(Long::intValue)
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertNotCompleted();
        assertThat(testSubscriber.getOnNextEvents()).isNotEmpty();
    }

    @Test
    public void shouldEmitLatestValueFromEndlessAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewLatestTransformer<>(view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));

        BehaviorSubject<Integer> subject = BehaviorSubject.create();
        subject.onNext(1);
        subject
                .subscribeOn(scheduler)
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertValue(1);
        testSubscriber.assertNotCompleted();
    }

    @Test
    public void shouldEmitSameLatestValueFromEndlessAfterViewIsAttachedAgain() {
        view.onNext(true);
        transformer = new WaitViewLatestTransformer<>(view
                .delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));
        BehaviorSubject<Integer> subject = BehaviorSubject.create();
        subject.onNext(1);
        subject
                .subscribeOn(scheduler)
                .compose(transformer)
                .subscribe(testSubscriber);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertValue(1);
        testSubscriber.assertNotCompleted();
        view.onNext(false);
        view.onNext(true);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testSubscriber.assertValues(1, 1);
        testSubscriber.assertNotCompleted();
    }

}
