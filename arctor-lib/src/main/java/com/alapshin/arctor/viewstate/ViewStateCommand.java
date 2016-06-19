package com.alapshin.arctor.viewstate;


import com.alapshin.arctor.view.MvpView;

import java.io.Serializable;

/**
 * ViewStateCommand interface
 *
 * The idea is that instead of directly calling view methods from presenter we use command pattern
 * and store commands in a queue. On orientation change queue saved into the {@link android.os.Bundle},
 * later queue restored from the {@link android.os.Bundle} and all commands executed in the order
 * they were added to queue.
 *
 * @param <V> view type
 */
public interface ViewStateCommand<V extends MvpView> extends Serializable {
    /**
     * Command type
     * @return command type
     */
    int type();

    /**
     * Command body
     * @param view {@link MvpView} against which command would be executed
     */
    void execute(V view);
}
