package com.alapshin.sampleconductor.di.modules;

import android.app.Application;
import android.content.Context;

import com.alapshin.sampleconductor.di.components.ApplicationComponent;
import com.alapshin.sampleconductor.di.qualifiers.ForApplication;
import com.alapshin.sampleconductor.di.scopes.ScopeIn;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private Application app;

    public ApplicationModule(Application app) {
        this.app = app;
    }

    @Provides
    @ScopeIn(ApplicationComponent.class)
    Application provideApplication() {
        return this.app;
    }

    @Provides
    @ForApplication
    @ScopeIn(ApplicationComponent.class)
    Context provideApplicationContext() {
        return this.app;
    }
}
