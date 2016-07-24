package com.alapshin.sampleconductor.di.modules;

import com.alapshin.sampleconductor.di.components.ActivityComponent;
import com.alapshin.sampleconductor.di.scopes.ScopeIn;
import com.alapshin.sampleconductor.foo.presenter.FooPresenter;
import com.alapshin.sampleconductor.foo.presenter.FooPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class FooModule {
    @Provides
    @ScopeIn(ActivityComponent.class)
    FooPresenter provideFooPresenter() {
        return new FooPresenterImpl();
    }
}
