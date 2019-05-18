package com.alapshin.arctor.view;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.alapshin.arctor.presenter.TestPresenter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class ActivityTest {
    private TestPresenter mockPresenter;

    @Before
    public void setup() {
        mockPresenter = mock(TestPresenter.class);
        ActivityLifecycleMonitorRegistry.getInstance()
                .addLifecycleCallback((activity, stage) -> {
                    if (stage == Stage.PRE_ON_CREATE && activity instanceof MvpActivity) {
                        ((MvpActivity) activity).presenter = mockPresenter;
                    }
                });
    }

    @Test
    @Ignore
    public void presenterLifecycleEventsCalled() {
        InOrder inOrder = inOrder(mockPresenter);
        ActivityScenario scenario = ActivityScenario.launch(TestActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

        inOrder.verify(mockPresenter).onCreate(null);
        inOrder.verify(mockPresenter).attachView(any(TestView.class));
        inOrder.verify(mockPresenter).onStart();
        inOrder.verify(mockPresenter).onResume();
        inOrder.verify(mockPresenter).onPause();
        inOrder.verify(mockPresenter).onStop();
        inOrder.verify(mockPresenter).detachView();
        inOrder.verify(mockPresenter).onDestroy();
        inOrder.verifyNoMoreInteractions();
    }
}
