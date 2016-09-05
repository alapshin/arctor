package com.alapshin.sampleconductor.main;

import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.alapshin.di.ComponentCache;
import com.alapshin.di.ComponentCacheDelegate;
import com.alapshin.sampleconductor.R;
import com.alapshin.sampleconductor.di.HasComponent;
import com.alapshin.sampleconductor.di.components.ActivityComponent;
import com.alapshin.sampleconductor.di.components.ApplicationComponent;
import com.alapshin.sampleconductor.di.modules.ActivityModule;
import com.alapshin.sampleconductor.foo.view.FooController;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements HasComponent<ActivityComponent>, ComponentCache {

    private Router router;
    private ActivityComponent component;
    private final ComponentCacheDelegate componentCacheDelegate = new ComponentCacheDelegate();

    @BindView(R.id.main_container) FrameLayout container;

    @Override
    public ActivityComponent component() {
        component = getComponent(0);
        if (component == null) {
            component = ((HasComponent<ApplicationComponent>) getApplication())
                    .component().activityComponent(new ActivityModule(this));
            setComponent(0, component);
        }
        return component;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        componentCacheDelegate.onCreate(savedInstanceState, getLastCustomNonConfigurationInstance());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new FooController()));
        }
    }

    @Override
    @CallSuper
    public Object onRetainCustomNonConfigurationInstance() {
        return componentCacheDelegate.onRetainCustomNonConfigurationInstance();
    }

    @Override
    public long generateComponentId() {
        return componentCacheDelegate.generateId();
    }

    @Override
    public <C> C getComponent(long index) {
        return componentCacheDelegate.getComponent(index);
    }

    @Override
    public <C> void setComponent(long index, C component) {
        componentCacheDelegate.setComponent(0, component);
    }
}
