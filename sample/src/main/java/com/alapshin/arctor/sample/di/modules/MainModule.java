package com.alapshin.arctor.sample.di.modules;

import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;
import com.alapshin.arctor.sample.main.presenter.MainPresenter;
import com.alapshin.arctor.sample.main.presenter.MainPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    @Provides
    @ScopeIn(ActivityComponent.class)
    MainPresenter provideMainPresenter() {
        return new MainPresenterImpl();
    }
}
