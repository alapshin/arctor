package com.alapshin.sampleconductor.di.components;


import com.alapshin.sampleconductor.CustomApplication;
import com.alapshin.sampleconductor.di.modules.ActivityModule;
import com.alapshin.sampleconductor.di.modules.ApplicationModule;
import com.alapshin.sampleconductor.di.scopes.ScopeIn;

import dagger.Component;

@Component(modules = {
        ApplicationModule.class,
})
@ScopeIn(ApplicationComponent.class)
public interface ApplicationComponent {
    final class Builder {
        public static ApplicationComponent build(CustomApplication app) {
            return DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(app))
                    .build();
        }

        private Builder() {}
    }

    void inject(CustomApplication application);

    ActivityComponent activityComponent(ActivityModule activityModule);
}
