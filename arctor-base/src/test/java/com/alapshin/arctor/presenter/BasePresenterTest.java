package com.alapshin.arctor.presenter;


import com.alapshin.arctor.view.MvpView;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BasePresenterTest {
    @Test
    public void shouldAttachView() {
        MvpView view = mock(MvpView.class);
        BasePresenter<MvpView> presenter = new BasePresenter<>();
        presenter.attachView(view);
        assertThat(presenter.getView()).isNotNull();
        assertThat(presenter.isViewAttached()).isTrue();
    }

    @Test
    public void shouldDetachView() {
        MvpView view = mock(MvpView.class);
        BasePresenter<MvpView> presenter = new BasePresenter<>();

        presenter.attachView(view);
        assertThat(presenter.getView()).isNotNull();
        assertThat(presenter.isViewAttached()).isTrue();

        presenter.detachView();
        assertThat(presenter.getView()).isNull();
        assertThat(presenter.isViewAttached()).isFalse();
    }

    @Test
    public void shouldDetectOrientationChange() {
        BasePresenter<MvpView> presenter = new BasePresenter<>();
        presenter.onCreate(null);
        presenter.onCreate(new PresenterBundle());
        assertThat(presenter.isOrientationChanged()).isTrue();
    }

    @Test
    public void shouldNotDetectOrienationChangeOnProcessRestart() {
        BasePresenter<MvpView> presenter = new BasePresenter<>();
        presenter.onCreate(new PresenterBundle());
        assertThat(presenter.isOrientationChanged()).isFalse();
    }
}
