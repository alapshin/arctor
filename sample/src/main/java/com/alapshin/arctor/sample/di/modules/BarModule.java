package com.alapshin.arctor.sample.di.modules;

import com.alapshin.arctor.sample.bar.presenter.BarPresenter;
import com.alapshin.arctor.sample.bar.presenter.BarPresenterImpl;
import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;

import dagger.Module;
import dagger.Provides;

@Module
public class BarModule {
    @Provides
    @ScopeIn(ActivityComponent.class)
    BarPresenter provideBarPresenter() {
        return new BarPresenterImpl();
    }
}
