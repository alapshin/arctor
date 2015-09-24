package com.alapshin.arctor.sample.di.modules;

import com.alapshin.arctor.sample.di.scopes.ActivityScope;
import com.alapshin.arctor.sample.foo.presenter.FooPresenter;
import com.alapshin.arctor.sample.foo.presenter.FooPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class FooModule {
    @Provides
    @ActivityScope
    FooPresenter provideFooPresenter() {
        return new FooPresenterImpl();
    }
}
