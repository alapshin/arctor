package com.alapshin.arctor.sample.di.components;

import com.alapshin.arctor.sample.CustomApplication;
import com.alapshin.arctor.sample.di.modules.ActivityModule;
import com.alapshin.arctor.sample.di.modules.ApplicationModule;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;

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
