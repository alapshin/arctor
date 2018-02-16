package com.alapshin.arctor.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.view.MvpView;

import static com.alapshin.arctor.presenter.PresenterBundleUtil.getPresenterBundle;
import static com.alapshin.arctor.presenter.PresenterBundleUtil.setPresenterBundle;

public class FragmentMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements FragmentMvpDelegate<V, P> {

    private MvpCallback<V, P> callback;

    public FragmentMvpDelegateImpl(MvpCallback<V, P> callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        callback.getPresenter().onCreate(getPresenterBundle(savedInstanceState));
    }

    @Override
    public void onViewCreated(@Nullable Bundle savedInstanceState) {
        callback.getPresenter().attachView(callback.getMvpView());
    }

    @Override
    public void onStart() {
        callback.getPresenter().onStart();
    }

    @Override
    public void onResume() {
        callback.getPresenter().onResume();
    }

    @Override
    public void onPause(boolean isFinishing) {
        callback.getPresenter().onPause();
        if (isFinishing) {
            callback.getPresenter().detachView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        PresenterBundle bundle = new PresenterBundle();
        callback.getPresenter().onSaveInstanceState(bundle);
        setPresenterBundle(outState, bundle);
    }

    @Override
    public void onStop() {
        callback.getPresenter().onStop();
    }

    @Override
    public void onDestroyView() {
        callback.getPresenter().detachView();
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        if (!isChangingConfigurations) {
            callback.getPresenter().onDestroy();
        }
    }
}
