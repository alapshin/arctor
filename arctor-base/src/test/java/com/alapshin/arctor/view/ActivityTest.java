package com.alapshin.arctor.view;


import com.alapshin.arctor.BuildConfig;
import com.alapshin.arctor.presenter.PresenterBundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class ActivityTest {
    TestPresenter mockPresenter;
    ActivityController<TestActivity> activityController;

    @Before
    public void setup() {
        mockPresenter = mock(TestPresenter.class);
        activityController = Robolectric.buildActivity(TestActivity.class);
        TestActivity activity = activityController.get();
        activity.presenter = mockPresenter;
    }

    @Test
    public void presenterLifecycleEventsCalled() {
        InOrder inOrder = inOrder(mockPresenter);

        activityController
                .create()
                .start()
                .resume()
                .pause()
                .stop()
                .destroy();

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

    @Test
    public void presenterSurvivesOrientationChange() {
        InOrder inOrder = inOrder(mockPresenter);
        activityController
                .setup();
        activityController.get().recreate();

        inOrder.verify(mockPresenter).onCreate(null);
        inOrder.verify(mockPresenter).attachView(any(TestView.class));
        inOrder.verify(mockPresenter).onStart();
        inOrder.verify(mockPresenter).onResume();
        inOrder.verify(mockPresenter).onPause();
        inOrder.verify(mockPresenter).onStop();
        inOrder.verify(mockPresenter).detachView();
        // Check that presenter onCreate called with bundle
        inOrder.verify(mockPresenter).onCreate(any(PresenterBundle.class));
        inOrder.verify(mockPresenter).attachView(any(TestView.class));
        inOrder.verify(mockPresenter).onStart();
        inOrder.verify(mockPresenter).onResume();
        inOrder.verifyNoMoreInteractions();
    }
}
