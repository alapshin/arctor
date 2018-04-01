package com.alapshin.arctor.presenter.rxjava2;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class WaitViewReplayTransformerTest {
    private static final int DELAY_IN_SECONDS = 60;

    private BehaviorSubject<Boolean> view;
    private TestScheduler scheduler;
    private WaitViewReplayTransformer<Integer> transformer;

    @Before
    public void setup() {
        view = BehaviorSubject.create();
        scheduler = new TestScheduler();
        transformer = new WaitViewReplayTransformer<>(view);
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
    public void shouldEmitValueAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewReplayTransformer<>(
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
    public void shouldReplayAllValuesAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewReplayTransformer<>(
                view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));

        TestObserver<Integer> testObserver = Observable.just(0, 1, 2)
                .compose(transformer)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
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
    public void shouldEmitErrorWhenViewIsAttached() {
        view.onNext(true);

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
        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(transformer)
                .subscribeOn(scheduler)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.assertNoValues();
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitErrorAfterViewIsAttached() {
        view.onNext(true);
        transformer = new WaitViewReplayTransformer<>(
                view.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS, scheduler));

        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(transformer)
                .test();
        scheduler.advanceTimeBy(DELAY_IN_SECONDS, TimeUnit.SECONDS);
        testObserver.awaitTerminalEvent();
        testObserver.assertError(exception);
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldReplayAllValuesFromEndlessAfterViewIsAttached() {
        PublishSubject<Integer> data = PublishSubject.create();
        TestObserver<Integer> testObserver = data.compose(transformer).test();
        data.onNext(0);
        data.onNext(1);
        view.onNext(true);
        testObserver.assertValues(0, 1);
        testObserver.assertNotComplete();
    }
}
