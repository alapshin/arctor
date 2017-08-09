package com.alapshin.arctor.view;


import android.os.Bundle;

import com.alapshin.arctor.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class FragmentTest {
    TestPresenter mockPresenter;

    @Before
    public void setup() {
        mockPresenter = mock(TestPresenter.class);
    }

    @Test
    public void presenterLifecycleEventsCalled() {
        InOrder inOrder = inOrder(mockPresenter);
        TestFragment fragment = new TestFragment();
        fragment.presenter = mockPresenter;
        SupportFragmentController<TestFragment> fragmentController
                = SupportFragmentController.of(fragment);

        fragmentController.create();
        fragment.onViewCreated(null, null);
        fragmentController.start();
        fragmentController.resume();
        fragmentController.pause();
        fragmentController.stop();
        fragmentController.destroy();
        fragment.onDetach();

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
        Bundle outState = new Bundle();
        TestFragment fragment = new TestFragment();
        fragment.presenter = mockPresenter;
        SupportFragmentController fragmentController = SupportFragmentController.of(fragment);

        fragmentController.create();
        fragment.onViewCreated(null, null);
        fragmentController.start();
        fragmentController.resume();
        fragment.onSaveInstanceState(outState);
        fragmentController.pause();
        fragmentController.stop();
        fragmentController.destroy();

        inOrder.verify(mockPresenter).onCreate(null);
        inOrder.verify(mockPresenter).attachView(any(TestView.class));
        inOrder.verify(mockPresenter).onStart();
        inOrder.verify(mockPresenter).onResume();
        inOrder.verify(mockPresenter).onPause();
        inOrder.verify(mockPresenter).onStop();
        inOrder.verify(mockPresenter).detachView();

        // Simulate fragment recreation
        TestFragment recreatedFragment = new TestFragment();
        recreatedFragment.presenter = mockPresenter;
        SupportFragmentController recreatedFragmentController =
                SupportFragmentController.of(recreatedFragment);
        recreatedFragmentController.create(outState);
        recreatedFragment.onViewCreated(null, outState);
        recreatedFragmentController.start();
        recreatedFragmentController.resume();
        inOrder.verify(mockPresenter).onCreate(null);
        inOrder.verify(mockPresenter).attachView(any(TestView.class));
        inOrder.verify(mockPresenter).onStart();
        inOrder.verify(mockPresenter).onResume();
        inOrder.verifyNoMoreInteractions();
    }
}
