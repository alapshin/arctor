package com.alapshin.arctor.presenter;

import android.os.Bundle;

import com.alapshin.arctor.view.MvpView;

import static com.alapshin.arctor.presenter.PresenterBundleUtil.getPresenterBundle;
import static com.alapshin.arctor.presenter.PresenterBundleUtil.setPresenterBundle;

/**
 * @author alapshin
 * @since 2015-07-04
 */
public class PresenterDelegate<V extends MvpView, P extends Presenter<V>> {
    private P presenter;
    private boolean isDestroyedBySystem;

    /**
     * Call from {@link android.app.Activity#onCreate(android.os.Bundle)} or
     * {@link android.app.Fragment#onCreate(android.os.Bundle)}
     * @param presenter presenter instance
     * @param savedInstanceState savedInstanceState from activity or fragment
     */
    public void onCreate(P presenter, Bundle savedInstanceState) {
        this.presenter = presenter;
        this.presenter.onCreate(getPresenterBundle(savedInstanceState));
    }

    /**
     * Call from {@link android.app.Activity#onContentChanged()} or
     * {@link android.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)}
     * @param view
     */
    public void onViewCreated(V view) {
        presenter.attachView(view);
    }

    /**
     * Call from {@link android.app.Activity#onStart()} or {@link android.app.Fragment#onStart()}
     */
    public void onStart() {
        presenter.onStart();
    }

    /**
     * Call from {@link android.app.Activity#onResume()} or {@link android.app.Fragment#onResume()}
     */
    public  void onResume() {
        isDestroyedBySystem = false;
        presenter.onResume();
    }

    /**
     * Call from {@link android.app.Activity#onPause()} or {@link android.app.Fragment#onPause()}
     */
    public void onPause() {
        presenter.onPause();
    }

    /**
     * Call from {@link android.app.Activity#onSaveInstanceState(android.os.Bundle)} or
     * {@link android.app.Fragment#onSaveInstanceState(android.os.Bundle)}
     * Allows presenter to save its state to bundle
     * @param outState activity or fragment bundle
     */
    public void onSaveInstanceState(Bundle outState) {
        isDestroyedBySystem = true;
        PresenterBundle bundle = new PresenterBundle();
        presenter.onSaveInstanceState(bundle);
        setPresenterBundle(outState, bundle);
    }


    /**
     * Call from {@link android.app.Activity#onStop} or {@link android.app.Fragment#onStop()}
     */
    public void onStop() {
        presenter.onStop();
    }

    public void onDestroyView() {
        presenter.detachView();
    }

    public void onDestroy() {
        if (!isDestroyedBySystem) {
            presenter.onDestroy();
        }
    }
}
