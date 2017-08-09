package com.alapshin.arctor.sample.main.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.alapshin.arctor.sample.R;
import com.alapshin.arctor.sample.bar.BarScreen;
import com.alapshin.arctor.sample.baz.BazScreen;
import com.alapshin.arctor.sample.di.HasComponent;
import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.arctor.sample.di.components.ApplicationComponent;
import com.alapshin.arctor.sample.di.modules.ActivityModule;
import com.alapshin.arctor.sample.foo.FooScreen;
import com.alapshin.arctor.sample.main.presenter.MainPresenter;
import com.alapshin.arctor.sample.navigation.Navigator;
import com.alapshin.mvp.view.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainView, MainPresenter>
        implements MainView, HasComponent<ActivityComponent> {

    @Inject
    Navigator navigator;

    @Inject
    ActivityComponent component;

    @BindView(R.id.activity_main_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_navigation_view) NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_bar:
                    navigator.set(BarScreen.create(), false);
                    break;
                case R.id.menu_baz:
                    navigator.set(BazScreen.create(), false);
                    break;
                case R.id.menu_foo:
                    navigator.set(FooScreen.create(), false);
                    break;
                default:
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigator.attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        navigator.detach();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return component;
    }

    @Override
    public ActivityComponent component() {
        return component;
    }

    @Override
    public void setData(long data) {
        toolbar.setTitle("Data " + data);
    }

    @Override
    protected void injectDependencies() {
        if (getLastCustomNonConfigurationInstance() != null) {
            component = ((ActivityComponent) getLastCustomNonConfigurationInstance());
            component.inject(this);
        } else {
            ((HasComponent<ApplicationComponent>) getApplication())
                    .component().activityComponent(new ActivityModule(this)).inject(this);
        }
    }

    @Override
    @LayoutRes
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }
}
