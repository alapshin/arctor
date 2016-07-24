package com.alapshin.arctor.presenter;


import android.os.Bundle;

import com.alapshin.arctor.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.assertj.core.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PresenterBundleUtilTest {
    @Test
    public void checkBundleInteraction() {
        Bundle outState = new Bundle();
        PresenterBundle presenterBundle = new PresenterBundle();
        presenterBundle.putString("foo", "bar");
        PresenterBundleUtil.setPresenterBundle(outState, presenterBundle);
        PresenterBundle presenterBundle1 = PresenterBundleUtil.getPresenterBundle(outState);
        assertThat(presenterBundle1.getString("foo")).isEqualTo("bar");
    }
}
