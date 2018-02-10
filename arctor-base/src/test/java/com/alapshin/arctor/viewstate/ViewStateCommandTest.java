package com.alapshin.arctor.viewstate;

import android.os.Bundle;

import com.alapshin.arctor.presenter.PresenterBundle;
import com.alapshin.arctor.presenter.PresenterBundleUtil;
import com.alapshin.arctor.view.MvpView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
public class ViewStateCommandTest {
    @Test
    public void serialize() {
        ViewStateCommand<MvpView> command = new ViewStateCommand<MvpView>() {
            public int foo = 1;
            public String bar = "bar";

            @Override
            public int type() {
                return 0;
            }

            @Override
            public void execute(MvpView view) {
            }
        };

        ViewStateCommandQueue<MvpView> commandQueue = new ViewStateCommandQueue<>();
        commandQueue.add(command);
        PresenterBundle presenterBundle = new PresenterBundle();
        presenterBundle.putSerializable("view_state_command_queue", commandQueue);
        Bundle bundle = new Bundle();
        PresenterBundleUtil.setPresenterBundle(bundle, presenterBundle);
        PresenterBundle restoredPresenterBundle = PresenterBundleUtil.getPresenterBundle(bundle);

        ViewStateCommandQueue<MvpView> restoredCommandQueue = (ViewStateCommandQueue<MvpView>)
                restoredPresenterBundle.getSerializable("view_state_command_queue");

        assertThat(restoredCommandQueue).isEqualTo(commandQueue);
    }
}
