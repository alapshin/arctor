package com.alapshin.arctor.sample.di.components;

import com.alapshin.arctor.sample.CustomApplication;
import com.alapshin.arctor.sample.di.modules.ActivityModule;
import com.alapshin.arctor.sample.di.modules.ApplicationModule;
import com.alapshin.arctor.sample.di.scopes.ApplicationScope;

import dagger.Component;


@ApplicationScope
@Component(modules = {
        ApplicationModule.class,
})
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
