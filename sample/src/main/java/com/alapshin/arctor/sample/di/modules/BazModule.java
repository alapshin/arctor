package com.alapshin.arctor.sample.di.modules;

import com.alapshin.arctor.sample.baz.presenter.BazPresenter;
import com.alapshin.arctor.sample.baz.presenter.BazPresenterImpl;
import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;

import dagger.Module;
import dagger.Provides;

@Module
public class BazModule {
    @Provides
    @ScopeIn(ActivityComponent.class)
    BazPresenter provideBazPresenter() {
        return new BazPresenterImpl();
    }
}
