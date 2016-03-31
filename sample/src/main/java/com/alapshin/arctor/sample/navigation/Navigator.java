package com.alapshin.arctor.sample.navigation;

import android.support.v4.app.FragmentActivity;

import javax.annotation.Nonnull;

/**
 * Navigation manager
 */
public interface Navigator {
    void attach(@Nonnull FragmentActivity activity);
    void detach();
    /**
     * Go to some screen
     * @param screen screen to go
     * @param addToBackStack add screen fragment to backstack or not
     */
    void set(Object screen, boolean addToBackStack);
}
