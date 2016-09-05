package com.alapshin.sampleconductor;

import android.app.Application;
import android.os.StrictMode;

import com.alapshin.sampleconductor.di.HasComponent;
import com.alapshin.sampleconductor.di.components.ApplicationComponent;


public class CustomApplication extends Application implements HasComponent<ApplicationComponent> {
    private ApplicationComponent component;

    @Override
    public ApplicationComponent component() {
        if (component == null) {
            component = ApplicationComponent.Builder.build(this);
        }
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setupDagger();
        if (BuildConfig.DEBUG) {
            setupStrictMode();
        }
    }

    protected void setupDagger() {
        component().inject(this);
    }

    protected void setupStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build());
        }
    }
}
