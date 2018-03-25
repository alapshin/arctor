package com.alapshin.arctor.presenter.rxjava2;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.BehaviorSubject;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitViewLatestTransformerTest {
    private static final int WAIT_DELAY_IN_SECONDS = 60;
    private static final int EMIT_DELAY_IN_SECONDS = 60;

    @Test
    public void shouldEmitValueWhenViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<>(view);

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
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<>(view);

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
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<>(view);

        TestScheduler testScheduler = new TestScheduler();
        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(transformer)
                .subscribeOn(testScheduler)
                .test();
        testScheduler.advanceTimeBy(WAIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertNoValues();
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitValueAfterViewIsAttached() {
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<>(view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));

        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(transformer)
                .test();
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(0);
        testObserver.assertComplete();
    }

    @Test
    public void shouldNotEmitValuesAfterViewIsDetached() {
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewLatestTransformer<Long> transformer =
                new WaitViewLatestTransformer<>(view);
        TestObserver<Long> testObserver = Observable.interval(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler)
                .compose(transformer)
                .test();
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValueCount(1);
        view.onNext(false);
        testScheduler.advanceTimeBy(2 * EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValueCount(1);
    }

    @Test
    public void shouldEmitLatestValueAfterViewIsAttached() {
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<>(view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));

        TestObserver<Integer> testObserver = Observable.fromArray(0, 1, 2)
                .compose(transformer)
                .test();
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(2);
        testObserver.assertComplete();
    }

    @Test
    public void shouldEmitErrorWhenViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<Integer>(view);

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
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<>(view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));

        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(transformer)
                .test();
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertError(exception);
    }

    @Test
    public void shouldNotEmitErrorWhenViewIsDetached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<Integer>(view);

        TestObserver<Integer> testObserver = Observable.<Integer>error(new RuntimeException())
                .compose(transformer)
                .test();
        testObserver.awaitTerminalEvent(EMIT_DELAY_IN_SECONDS, TimeUnit.MILLISECONDS);
        testObserver.assertNoErrors();
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitValueFromEndlessAfterViewIsAttached() {
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewLatestTransformer<Long> transformer =
                new WaitViewLatestTransformer<>(view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));
        TestObserver<Long> testObserver =
                Observable.interval(0, 100, TimeUnit.SECONDS, testScheduler)
                        .compose(transformer)
                        .test();
        testScheduler.advanceTimeBy(500, TimeUnit.SECONDS);
        testObserver.assertNotComplete();
        assertThat(testObserver.getEvents()).isNotEmpty();
    }

    @Test
    public void shouldEmitLatestValueFromEndlessAfterViewIsAttached() {
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<>(view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));

        BehaviorSubject<Integer> subject = BehaviorSubject.create();
        subject.onNext(1);
        TestObserver<Integer> testObserver = subject
                .compose(transformer)
                .test();
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValue(1);
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldAgainEmitLatestValueFromEndlessAfterViewIsAttached() {
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewLatestTransformer<Integer> transformer =
                new WaitViewLatestTransformer<>(view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));

        BehaviorSubject<Integer> subject = BehaviorSubject.create();
        subject.onNext(1);
        TestObserver<Integer> testObserver = subject.compose(transformer)
                .test();
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValue(1);
        testObserver.assertNotComplete();
        view.onNext(false);
        view.onNext(true);
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertValues(1, 1);
        testObserver.assertNotComplete();
    }
}
