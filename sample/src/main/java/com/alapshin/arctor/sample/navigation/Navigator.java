package com.alapshin.arctor.sample.navigation;

/**
 * Navigation manager
 */
public interface Navigator {
    /**
     * Go to some screen
     * @param screen screen to go
     * @param addToBackStack add screen fragment to backstack or not
     */
    void set(Object screen, boolean addToBackStack);
}
