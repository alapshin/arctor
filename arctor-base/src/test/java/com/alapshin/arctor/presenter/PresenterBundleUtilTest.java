package com.alapshin.arctor.presenter;


import android.os.Bundle;

import com.alapshin.arctor.BuildConfig;
import com.alapshin.arctor.LibraryRobolectricTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import static org.assertj.core.api.Assertions.*;

@RunWith(LibraryRobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
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
