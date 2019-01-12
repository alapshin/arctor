package com.alapshin.arctor.sample.di.modules;

import android.app.Application;
import android.content.Context;

import com.alapshin.arctor.sample.di.components.ApplicationComponent;
import com.alapshin.arctor.sample.di.qualifiers.ForApplication;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;

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
