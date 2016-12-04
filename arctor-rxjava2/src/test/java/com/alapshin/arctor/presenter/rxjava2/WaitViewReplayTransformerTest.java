package com.alapshin.arctor.presenter.rxjava2;


import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.BehaviorSubject;

public class WaitViewReplayTransformerTest {
    @Test
    public void shouldEmitValueWhenViewIsAttached() {
        BehaviorSubject<Boolean> viewValve = BehaviorSubject.create();

        viewValve.onNext(true);
        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(new WaitViewReplayTransformer<Integer>(viewValve))
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(0);
        testObserver.assertComplete();
    }

    @Test
    public void shouldEmitValueAfterViewIsAttached() {
        BehaviorSubject<Boolean> viewValve = BehaviorSubject.create();

        viewValve.onNext(true);
        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(new WaitViewReplayTransformer<Integer>(viewValve.delay(200, TimeUnit.MILLISECONDS)))
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(0);
        testObserver.assertComplete();
    }

    @Test
    public void shouldReplayValuesAfterViewIsAttached() {
        BehaviorSubject<Boolean> viewValve = BehaviorSubject.create();

        viewValve.onNext(true);
        TestObserver<Integer> testObserver = Observable.fromArray(0, 1, 2)
                .compose(new WaitViewReplayTransformer<Integer>(viewValve.delay(200, TimeUnit.MILLISECONDS)))
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertValues(0, 1, 2);
        testObserver.assertComplete();
    }

    @Test
    public void shouldNotEmitValueWhenViewIsDetached() {
        BehaviorSubject<Boolean> viewValve = BehaviorSubject.create();

        viewValve.onNext(false);
        TestObserver<Integer> testObserver = Observable.just(0)
                .compose(new WaitViewReplayTransformer<Integer>(viewValve))
                .test();
        testObserver.awaitTerminalEvent(200, TimeUnit.MILLISECONDS);
        testObserver.assertNoValues();
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitErrorWhenViewIsAttached() {
        BehaviorSubject<Boolean> viewValve = BehaviorSubject.create();

        viewValve.onNext(true);
        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(new WaitViewReplayTransformer<Integer>(viewValve))
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(exception);
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldEmitErrorAfterViewIsAttached() {
        BehaviorSubject<Boolean> viewValve = BehaviorSubject.create();

        viewValve.onNext(true);
        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(new WaitViewReplayTransformer<Integer>(viewValve.delay(200, TimeUnit.MILLISECONDS)))
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(exception);
        testObserver.assertNotComplete();
    }

    @Test
    public void shouldNotEmitErrorWhenViewIsDetached() {
        BehaviorSubject<Boolean> viewValve = BehaviorSubject.create();

        viewValve.onNext(false);
        Exception exception = new RuntimeException();
        TestObserver<Integer> testObserver = Observable.<Integer>error(exception)
                .compose(new WaitViewReplayTransformer<Integer>(viewValve))
                .test();
        testObserver.awaitTerminalEvent(200, TimeUnit.MILLISECONDS);
        testObserver.assertNoValues();
        testObserver.assertNotComplete();
    }
}
