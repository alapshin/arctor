package com.alapshin.arctor.sample.di.modules;

import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;
import com.alapshin.arctor.sample.foo.presenter.FooPresenter;
import com.alapshin.arctor.sample.foo.presenter.FooPresenterImpl;

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
