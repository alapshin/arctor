package com.alapshin.arctor.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


/**
 * Базовый класс активности
 *
 * @author alapshin
 * @since 2015-05-06
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies();
        setContentView(getLayoutRes());
    }

    @Override
    @CallSuper
    public void onContentChanged() {
        ButterKnife.bind(this);
    }


    @Override
    @CallSuper
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    @CallSuper
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    protected abstract void injectDependencies();
    protected abstract @LayoutRes int getLayoutRes();
}
