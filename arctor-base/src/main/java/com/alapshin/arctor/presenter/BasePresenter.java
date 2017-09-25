package com.alapshin.arctor.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alapshin.arctor.view.MvpView;
import com.alapshin.arctor.viewstate.ViewStateCommand;
import com.alapshin.arctor.viewstate.ViewStateCommandQueue;

import java.lang.ref.WeakReference;

/**
 * Base presenter implementation
 *
 * @author alapshin
 * @since 2015-04-18
 */
public class BasePresenter<V extends MvpView> implements Presenter<V> {
    private static final String VIEW_STATE_COMMAND_QUEUE_KEY = "view_state_command_queue";

    /**
     * How to store command into the queue after execution
     */
    public enum CommandStoreStrategy {
        /**
         * Add command to the end of the queue
         */
        ADD,
        /**
         * Clear the queue and add command the end of queue
         */
        CLEAR,
        /**
         * Add command to the end of the queue but remove previous commands of the same type from the queue
         */
        REPLACE,
        /**
         * Don't add command to the queue
         */
        SKIP
    }

    private boolean isViewRecreated = false;
    private boolean wasViewOnCreateCalled = false;
    /**
     * Reference to view.
     * Using weak reference to avoid memory leaks.
     * Before calling any view methods check that view is attached with isViewAttached
     */
    protected WeakReference<V> viewRef;
    protected ViewStateCommandQueue<V> viewStateCommandQueue = new ViewStateCommandQueue<>();

    @Override
    @CallSuper
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable PresenterBundle savedInstanceState) {
        // Method onCreate could be called in 3 different cases
        // 1. After corresponding view is created for the first time
        // 2. After corresponding view is recreated after configuration change
        // 3. After corresponding view is recreated after process death

        // In first case savedInstanceState is null, while in second and third case it is not.
        // As a result to distinguish between later two cases checking for savedInstanceState alone is not enough.
        // To solve this problem additional flag is introduced that could be checked when savedInstanceState is not null.
        // When presenter's onCreate is called after configuration change this flag will be true and false otherwise.
        if (wasViewOnCreateCalled) {
           isViewRecreated = true;
        } else {
            isViewRecreated = false;
            wasViewOnCreateCalled = true;
        }

        if (savedInstanceState != null) {
            viewStateCommandQueue = (ViewStateCommandQueue<V>) savedInstanceState.getSerializable(
                    VIEW_STATE_COMMAND_QUEUE_KEY);
        }
    }

    @Override
    @CallSuper
    public void onStart() {
    }

    @Override
    @CallSuper
    public void onResume() {
    }

    @Override
    @CallSuper
    public void onPause() {
    }

    @Override
    @CallSuper
    public void onSaveInstanceState(@NonNull PresenterBundle outState) {
        outState.putSerializable(VIEW_STATE_COMMAND_QUEUE_KEY, viewStateCommandQueue);
    }

    @Override
    @CallSuper
    public void onStop() {
    }

    @Override
    @CallSuper
    public void onDestroy() {
        viewStateCommandQueue.clear();
    }

    @Override
    @CallSuper
    public void attachView(V view) {
        this.viewRef = new WeakReference<V>(view);
        if (viewStateCommandQueue != null) {
            for (ViewStateCommand<V> command: viewStateCommandQueue) {
                command.execute(view);
            }
        }
    }

    @Override
    @CallSuper
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    /**
     * Return currently attached view
     * @return attached view view or null if view is detached
     */
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    public boolean isViewRecreated() {
        return isViewRecreated;
    }

    /**
     * Execute command against view and store command into the queue
     * @param command command to execute
     * @param strategy store strategy
     */
    public void executeCommand(ViewStateCommand<V> command, CommandStoreStrategy strategy) {
        command.execute(getView());
        switch (strategy) {
            case ADD:
                viewStateCommandQueue.add(command);
                break;
            case CLEAR:
                viewStateCommandQueue.addWithClear(command);
            case REPLACE:
                viewStateCommandQueue.addWithReplace(command);
                break;
            case SKIP:
                break;
            default:
                break;
        }
    }
}
