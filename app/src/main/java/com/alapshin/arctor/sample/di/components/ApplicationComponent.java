package com.alapshin.arctor.sample.di.components;

import com.alapshin.arctor.sample.CustomApplication;
import com.alapshin.arctor.sample.di.modules.MainModule;
import com.alapshin.arctor.sample.di.modules.SystemServicesModule;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        MainModule.class,
        SystemServicesModule.class,

        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
})
@ScopeIn(ApplicationComponent.class)
public interface ApplicationComponent {
    void inject(CustomApplication application);
}
