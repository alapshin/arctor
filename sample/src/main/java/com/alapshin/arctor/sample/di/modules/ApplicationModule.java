package com.alapshin.arctor.sample.di.modules;

import android.app.Application;
import android.content.Context;

import com.alapshin.arctor.sample.di.qualifiers.ForApplication;
import com.alapshin.arctor.sample.di.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private Application app;

    public ApplicationModule(Application app) {
        this.app = app;
    }

    @Provides
    @ApplicationScope
    Application provideApplication() {
        return this.app;
    }

    @Provides
    @ForApplication
    @ApplicationScope
    Context provideApplicationContext() {
        return this.app;
    }
}
