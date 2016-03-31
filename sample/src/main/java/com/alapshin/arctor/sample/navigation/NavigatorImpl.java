package com.alapshin.arctor.sample.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.alapshin.arctor.sample.R;
import com.alapshin.arctor.sample.bar.BarScreen;
import com.alapshin.arctor.sample.bar.view.BarFragment;
import com.alapshin.arctor.sample.baz.BazScreen;
import com.alapshin.arctor.sample.baz.view.BazViewGroup;
import com.alapshin.arctor.sample.foo.FooScreen;
import com.alapshin.arctor.sample.foo.view.FooFragment;

import javax.annotation.Nonnull;

/**
 * {@link Navigator} implementation using fragments for screens
 */
public class NavigatorImpl implements Navigator {
    private FragmentActivity activity;

    @Override
    public void attach(@Nonnull FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void detach() {
        this.activity = null;
    }

    @Override
    public void set(Object screen, boolean addToBackStack) {
        Fragment fragment = null;
        ViewGroup viewGroup = null;

        if (screen instanceof BarScreen) {
            fragment = new BarFragment();
        } else if (screen instanceof BazScreen) {
            viewGroup = new BazViewGroup(activity);
        } else if (screen instanceof FooScreen) {
            fragment = new FooFragment();
        }

        ViewGroup container = (ViewGroup) activity.findViewById(R.id.activity_main_fragment_container);
        if (fragment != null) {
            container.removeAllViews();
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_fragment_container, fragment);
            if (addToBackStack) {
                ft.addToBackStack(null);
            }
            ft.commit();
        } else if (viewGroup != null) {
            container.removeAllViews();
            container.addView(viewGroup);
        }
    }
}
