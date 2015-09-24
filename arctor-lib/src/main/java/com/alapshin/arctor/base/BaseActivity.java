package com.alapshin.arctor.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import butterknife.ButterKnife;
import icepick.Icepick;


/**
 * Базовый класс активности
 *
 * @author alapshin
 * @since 2015-05-06
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        injectDependencies();
        setContentView(getLayoutRes());
    }

    @Override
    public void onContentChanged() {
        ButterKnife.bind(this);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
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
