package com.alapshin.arctor.sample.di.components;


import com.alapshin.arctor.sample.bar.view.BarFragment;
import com.alapshin.arctor.sample.baz.view.BazViewGroup;
import com.alapshin.arctor.sample.di.modules.ActivityModule;
import com.alapshin.arctor.sample.di.modules.BarModule;
import com.alapshin.arctor.sample.di.modules.BazModule;
import com.alapshin.arctor.sample.di.modules.FooModule;
import com.alapshin.arctor.sample.di.modules.MainModule;
import com.alapshin.arctor.sample.di.modules.NavigationModule;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;
import com.alapshin.arctor.sample.foo.view.FooFragment;
import com.alapshin.arctor.sample.main.view.MainActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {
        ActivityModule.class,
        FooModule.class,
        BarModule.class,
        BazModule.class,
        MainModule.class,
        NavigationModule.class
})
@ScopeIn(ActivityComponent.class)
public interface ActivityComponent {
    void inject(BarFragment fragment);
    void inject(FooFragment fragment);
    void inject(MainActivity activity);
    void inject(BazViewGroup viewGroup);
}
