package com.alapshin.sampleconductor.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

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
        implements HasComponent<ActivityComponent> {

    private Router router;
    private ActivityComponent component;

    @BindView(R.id.main_container) FrameLayout container;

    @Override
    public ActivityComponent component() {
        if (component == null) {
            component = ((HasComponent<ApplicationComponent>) getApplication())
                    .component().activityComponent(new ActivityModule(this));
        }
        return component;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new FooController()));
        }
    }
}
