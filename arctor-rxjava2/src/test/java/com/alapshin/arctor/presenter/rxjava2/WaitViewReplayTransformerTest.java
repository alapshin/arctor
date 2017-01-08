package com.alapshin.arctor.presenter.rxjava2;


import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class WaitViewReplayTransformerTest {
    private static final int EMIT_DELAY_IN_SECONDS = 60;
    private static final int WAIT_DELAY_IN_MILLISECONDS = 60;

    @Test
    public void shouldEmitValueWhenViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(view);

        view.onNext(true);
        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(transformer)
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(0);
        testObserver.assertComplete();
    }

    @Test
    public void shouldEmitValueAfterViewIsAttached() {
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(
                view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));

        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(transformer)
                .test();
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(0);
        testObserver.assertComplete();
    }

    @Test
    public void shouldReplayAllValuesAfterViewIsAttached() {
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(
                view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));

        TestObserver<Integer> testObserver = Observable.just(0, 1, 2)
                .compose(transformer)
                .test();
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertValues(0, 1, 2);
        testObserver.assertComplete();
    }

    @Test
    public void shouldNotEmitValueWhenViewIsDetached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(view);

        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(transformer)
                .test();
        testObserver.awaitTerminalEvent(WAIT_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testObserver.assertNoValues();
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitErrorWhenViewIsAttached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(view);

        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(transformer)
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(exception);
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldNotEmitErrorWhenViewIsDetached() {
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(view);

        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(transformer)
                .test();
        testObserver.awaitTerminalEvent(WAIT_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        testObserver.assertNoValues();
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitErrorAfterViewIsAttached() {
        TestScheduler testScheduler = new TestScheduler();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        view.onNext(true);
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(
                view.delay(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS, testScheduler));

        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(transformer)
                .test();
        testScheduler.advanceTimeBy(EMIT_DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertError(exception);
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldReplayAllValuesFromEndlessAfterViewIsAttached() {
        PublishSubject<Integer> data = PublishSubject.create();
        BehaviorSubject<Boolean> view = BehaviorSubject.create();
        WaitViewReplayTransformer<Integer> transformer = new WaitViewReplayTransformer<>(view);
        TestObserver<Integer> testObserver = data.compose(transformer).test();
        data.onNext(0);
        data.onNext(1);
        view.onNext(true);
        testObserver.assertValues(0, 1);
        testObserver.assertNotComplete();
    }
}
