package com.alapshin.arctor.util;

import android.os.Bundle;
import android.util.Log;

import com.alapshin.arctor.presenter.PresenterBundle;

import java.util.HashMap;

import javax.annotation.Nullable;

public class PresenterBundleUtil {
    private static final String TAG = PresenterBundleUtil.class.getSimpleName();
    private static final String MAP_KEY = PresenterBundle.class.getName();

    private PresenterBundleUtil() {
        // No instances
    }

    /**
     * Read {@link PresenterBundle} from android {@link Bundle}
     * @param savedInstanceState android bundle
     * @return presenter bundle
     */
    @SuppressWarnings("unchecked") // Handled internally
    public static PresenterBundle getPresenterBundle(@Nullable Bundle savedInstanceState) {
        HashMap<String, Object> map = null;
        if (savedInstanceState != null) {
            try {
                map = (HashMap<String, Object>) savedInstanceState
                        .getSerializable(MAP_KEY);
            } catch (ClassCastException e) {
                Log.e(TAG, "", e);
            }
        }
        PresenterBundle result = null;
        if (map != null) {
            result = new PresenterBundle();
            result.setMap(map);
        }
        return result;
    }

    /**
     * Write {$link PresenterBundle} to android {@link Bundle}
     * @param outState android bundle
     * @param presenterBundle presenter bundle
     */
    public static void setPresenterBundle(Bundle outState, PresenterBundle presenterBundle) {
        outState.putSerializable(MAP_KEY, presenterBundle.getMap());
    }
}
