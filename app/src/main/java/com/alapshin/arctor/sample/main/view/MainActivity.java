package com.alapshin.arctor.sample.main.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.alapshin.arctor.sample.R;
import com.alapshin.arctor.sample.bar.view.BarFragment;
import com.alapshin.arctor.sample.di.modules.MainModule_ContributeYourActivityInjector;
import com.alapshin.arctor.sample.foo.view.FooFragment;
import com.alapshin.arctor.sample.main.presenter.MainPresenter;
import com.alapshin.mvp.view.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity<MainView, MainPresenter>
        implements MainView, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Inject
    MainModule_ContributeYourActivityInjector.MainActivitySubcomponent subcomponent;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_navigation_view)
    NavigationView navigationView;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.menu_foo:
                    fragment = new FooFragment();
                    break;
                case R.id.menu_bar:
                    fragment = new BarFragment();
                    break;
                default:
                    break;
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, fragment)
                        .commit();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    public void onData(long data) {
        toolbar.setTitle("Data " + data);
    }

    @Override
    public void onError(Throwable error) {
    }

    @Override
    public void onProgress() {
    }

    @Override
    @LayoutRes
    protected int getLayoutRes() {
        return R.layout.main_activity;
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return subcomponent;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    protected void injectDependencies() {
        Object subcomponent = getLastCustomNonConfigurationInstance();
        if (subcomponent == null) {
            AndroidInjection.inject(this);
        } else {
            ((MainModule_ContributeYourActivityInjector.MainActivitySubcomponent) subcomponent)
                    .inject(this);
        }
    }
}
