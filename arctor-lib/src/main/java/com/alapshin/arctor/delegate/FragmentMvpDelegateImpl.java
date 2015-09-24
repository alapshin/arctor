package com.alapshin.arctor.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.view.MvpView;

import javax.annotation.Nonnull;

import static com.alapshin.arctor.presenter.PresenterBundleUtil.getPresenterBundle;
import static com.alapshin.arctor.presenter.PresenterBundleUtil.setPresenterBundle;

public class FragmentMvpDelegateImpl<V extends MvpView, P extends Presenter<V>>
        implements FragmentMvpDelegate<V, P> {

    private P presenter;
    private boolean isDestroyedBySystem;

    @Override
    public void onCreate(@Nonnull P presenter, @Nullable Bundle savedInstanceState) {
        this.presenter = presenter;
        this.presenter.onCreate(getPresenterBundle(savedInstanceState));
    }

    @Override
    public void onViewCreated(V view, @Nullable Bundle savedInstanceState) {
        presenter.attachView(view);
    }

    @Override
    public void onStart() {
        presenter.onStart();
    }

    @Override
    public void onResume() {
        isDestroyedBySystem = false;
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        isDestroyedBySystem = true;
        PresenterBundle bundle = new PresenterBundle();
        presenter.onSaveInstanceState(bundle);
        setPresenterBundle(outState, bundle);
    }

    @Override
    public void onStop() {
        presenter.onStop();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
    }

    @Override
    public void onDestroy() {
        if (!isDestroyedBySystem) {
            presenter.onDestroy();
        }
    }
}
