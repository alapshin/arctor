package com.alapshin.arctor.sample.main.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.alapshin.arctor.sample.R;
import com.alapshin.arctor.sample.bar.view.BarFragment;
import com.alapshin.arctor.sample.di.HasComponent;
import com.alapshin.arctor.sample.di.components.ActivityComponent;
import com.alapshin.arctor.sample.di.components.ApplicationComponent;
import com.alapshin.arctor.sample.di.modules.ActivityModule;
import com.alapshin.arctor.sample.foo.view.FooFragment;
import com.alapshin.arctor.sample.main.presenter.MainPresenter;
import com.alapshin.mvp.view.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainView, MainPresenter>
        implements MainView, HasComponent<ActivityComponent> {

    @Inject
    ActivityComponent component;

    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.main_navigation_view) NavigationView navigationView;

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
    @SuppressWarnings("unchecked")
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
        return R.layout.main_activity;
    }
}
