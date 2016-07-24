package com.alapshin.arctor.presenter.rx;


import com.alapshin.arctor.view.MvpView;

import org.junit.Test;


import static org.mockito.Mockito.*;

public class RxPresenterTest {
    @Test
    public void sample() {
        MvpView view = mock(MvpView.class);
        RxPresenter<MvpView> presenter = new RxPresenter<>();
    }
}
