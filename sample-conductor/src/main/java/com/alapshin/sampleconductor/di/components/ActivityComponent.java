package com.alapshin.sampleconductor.di.components;


import com.alapshin.sampleconductor.di.modules.ActivityModule;
import com.alapshin.sampleconductor.di.modules.FooModule;
import com.alapshin.sampleconductor.di.scopes.ScopeIn;
import com.alapshin.sampleconductor.foo.view.FooController;

import dagger.Subcomponent;

@Subcomponent(modules = {
        ActivityModule.class,
        FooModule.class,
})
@ScopeIn(ActivityComponent.class)
public interface ActivityComponent {
    void inject(FooController controller);
}
