package com.alapshin.arctor.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.alapshin.arctor.presenter.TestPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class FragmentTest {
    private TestPresenter mockPresenter;
    private FragmentFactory fragmentFactory;

    @Before
    public void setup() {
        mockPresenter = mock(TestPresenter.class);
        fragmentFactory = new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader,
                                        @NonNull String className,
                                        @Nullable Bundle args) {
                TestFragment f = new TestFragment();
                f.presenter = mockPresenter;
                return  f;
            }
        };
    }

    @Test
    public void presenterLifecycleEventsCalled() {
        InOrder inOrder = inOrder(mockPresenter);
        FragmentScenario scenario = FragmentScenario.launchInContainer(
                TestFragment.class, null, fragmentFactory);
        scenario.moveToState(Lifecycle.State.DESTROYED);

        inOrder.verify(mockPresenter).onCreate(null);
//        inOrder.verify(mockPresenter).attachView(any(TestView.class));
        inOrder.verify(mockPresenter).onStart();
        inOrder.verify(mockPresenter).onResume();
        inOrder.verify(mockPresenter).onPause();
        inOrder.verify(mockPresenter).onStop();
//        inOrder.verify(mockPresenter).detachView();
        inOrder.verify(mockPresenter).onDestroy();
        inOrder.verifyNoMoreInteractions();
    }
}
