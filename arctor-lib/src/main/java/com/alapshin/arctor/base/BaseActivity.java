package com.alapshin.arctor.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import butterknife.ButterKnife;


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

        injectDependencies();
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
    }

    @Override
    public void onContentChanged() {
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    protected void openFragment(int containerViewId, String tag, boolean addToBackStack) {
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = Fragment.instantiate(this, tag);
        }
        openFragment(containerViewId, fragment, tag, addToBackStack);
    }

    protected void openFragment(int containerViewId, Fragment fragment, String tag,
                                boolean addToBackStack) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(containerViewId, fragment, tag);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    protected abstract void injectDependencies();
    protected abstract @LayoutRes int getLayoutRes();
}
