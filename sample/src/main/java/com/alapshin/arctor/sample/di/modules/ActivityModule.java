package com.alapshin.arctor.sample.di.modules;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.arctor.sample.di.qualifiers.ForActivity;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    FragmentActivity provideActivity() {
        return (FragmentActivity) this.activity;
    }

    @Provides
    @ForActivity
    @ScopeIn(ActivityComponent.class)
    Context provideActivityContext() {
        return this.activity;
    }
}
