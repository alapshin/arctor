package com.alapshin.arctor.presenter.rxjava2;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.BehaviorSubject;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitViewLatestTransformerTest {
    private static final int DELAY_IN_SECONDS = 60;

    private BehaviorSubject<Boolean> view;
    private TestScheduler scheduler;
    private WaitViewLatestTransformer<Integer> transformer;

    @Before
    public void setup() {
        view = BehaviorSubject.create();
        scheduler = new TestScheduler();
        transformer = new WaitViewLatestTransformer<>(view);
    }

    @Test
    public void shouldEmitValueWhenViewIsAttached() {
        view.onNext(true);
        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(transformer)
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(0);
        testObserver.assertComplete();
    }

    @Test
    public void shouldEmitAllValuesWhenViewIsAttached() {
        view.onNext(true);
        TestObserver<Integer> testObserver = Observable.just(0, 1, 2)
                .compose(transformer)
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertValues(0, 1, 2);
        testObserver.assertComplete();
    }

    @Test
    public void shouldNotEmitValueWhenViewIsDetached() {
        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(transformer)
                .subscribeOn(scheduler)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertNoValues();
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitValueAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewLatestTransformer<>(
                view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));

        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(transformer)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(0);
        testObserver.assertComplete();
    }

    @Test
    public void shouldNotEmitValuesAfterViewIsDetached() {
        view.onNext(true);
        TestObserver<Integer> testObserver = Observable.interval(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler)
                .map(Long::intValue)
                .compose(transformer)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValueCount(1);
        view.onNext(false);
        scheduler.advanceTimeBy(2 * DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValueCount(1);
    }

    @Test
    public void shouldEmitLatestValueAfterViewIsAttached() {
        view.onNext(true);
        transformer =
                new WaitViewLatestTransformer<>(view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));
        TestObserver<Integer> testObserver = Observable.fromArray(0, 1, 2)
                .compose(transformer)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(2);
        testObserver.assertComplete();
    }

    @Test
    public void shouldEmitErrorWhenViewIsAttached() {
        view.onNext(true);
        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(transformer)
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(exception);
    }

    @Test
    public void shouldEmitErrorAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewLatestTransformer<>(view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));

        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(transformer)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertError(exception);
    }

    @Test
    public void shouldNotEmitErrorWhenViewIsDetached() {
        TestObserver<Integer> testObserver = Observable.<Integer>error(new RuntimeException())
                .compose(transformer)
                .subscribeOn(scheduler)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertNoErrors();
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitValueFromEndlessAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewLatestTransformer<>(view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));
        TestObserver<Integer> testObserver =
                Observable.interval(0, 100, TimeUnit.SECONDS, scheduler)
                        .map(Long::intValue)
                        .compose(transformer)
                        .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertNotComplete();
        assertThat(testObserver.getEvents()).isNotEmpty();
    }

    @Test
    public void shouldEmitLatestValueFromEndlessAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewLatestTransformer<>(view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));

        BehaviorSubject<Integer> subject = BehaviorSubject.create();
        subject.onNext(1);
        TestObserver<Integer> testObserver = subject
                .compose(transformer)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValue(1);
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitSameLatestValueFromEndlessAfterViewIsAttachedAgain() {
        view.onNext(true);
        transformer = new WaitViewLatestTransformer<>(view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));

        BehaviorSubject<Integer> subject = BehaviorSubject.create();
        subject.onNext(1);
        TestObserver<Integer> testObserver = subject.compose(transformer)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValue(1);
        testObserver.assertNotComplete();
        view.onNext(false);
        view.onNext(true);
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValues(1, 1);
        testObserver.assertNotComplete();
    }
}
