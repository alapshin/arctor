package com.alapshin.arctor.sample.di.modules;

import android.support.v4.app.FragmentActivity;

import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;
import com.alapshin.arctor.sample.navigation.Navigator;
import com.alapshin.arctor.sample.navigation.NavigatorImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class NavigationModule {
    @Provides
    @ScopeIn(ActivityComponent.class)
    Navigator provideNavigator() {
        return new NavigatorImpl();
    }
}
