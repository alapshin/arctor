package com.alapshin.arctor.sample.di.modules;

import com.alapshin.arctor.sample.bar.presenter.BarPresenter;
import com.alapshin.arctor.sample.bar.presenter.BarPresenterImpl;
import com.alapshin.arctor.sample.bar.view.BarFragment;
import com.alapshin.arctor.sample.di.scopes.ScopeIn;
import com.alapshin.arctor.sample.foo.presenter.FooPresenter;
import com.alapshin.arctor.sample.foo.presenter.FooPresenterImpl;
import com.alapshin.arctor.sample.foo.view.FooFragment;
import com.alapshin.arctor.sample.main.presenter.MainPresenter;
import com.alapshin.arctor.sample.main.presenter.MainPresenterImpl;
import com.alapshin.arctor.sample.main.view.MainActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface MainModule {
    @ScopeIn(MainActivity.class)
    @ContributesAndroidInjector(modules = {
            DependenciesModule.class,
            FragmentInjectionModule.class
    })
    MainActivity contributeYourActivityInjector();

    @Module
    interface FragmentInjectionModule {
        @ContributesAndroidInjector
        BarFragment contributeBarFragmentInjector();
        @ContributesAndroidInjector
        FooFragment contributeFooFragmentInjector();
    }

    @Module
    interface DependenciesModule {
        @Binds
        @ScopeIn(MainActivity.class)
        BarPresenter bindBarPresenter(BarPresenterImpl presenter);

        @Binds
        @ScopeIn(MainActivity.class)
        FooPresenter bindFooPresenter(FooPresenterImpl presenter);

        @Binds
        @ScopeIn(MainActivity.class)
        MainPresenter bindMainPresenter(MainPresenterImpl presenter);

    }
}
