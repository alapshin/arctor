package com.alapshin.arctor.viewstate;


import com.alapshin.arctor.view.MvpView;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * Queue to store {@link ViewStateCommand}
 * @param <V> view type
 */
public class ViewStateCommandQueue<V extends MvpView>
        implements Serializable, Iterable<ViewStateCommand<V>> {

    private Queue<ViewStateCommand<V>> commands;

    public ViewStateCommandQueue() {
        commands = new ArrayDeque<>();
    }

    /**
     * Add command to the end
     * @param command command to add
     */
    public void add(ViewStateCommand<V> command) {
        commands.add(command);
    }

    public void addWithClear(ViewStateCommand<V> command) {
        commands.clear();
        commands.add(command);
    }

    /**
     * Add command to the end, but remove commands of the same type if they already in queue
     * @param command command to add
     */
    public void addWithReplace(ViewStateCommand<V> command) {
        for (Iterator<ViewStateCommand<V>> it = commands.iterator(); it.hasNext(); ) {
            ViewStateCommand<V> command1 = it.next();
            if (command1.type() == command.type()) {
                it.remove();
            }
        }
        commands.add(command);
    }

    public ViewStateCommand<V> remove() {
        return commands.remove();
    }

    public ViewStateCommand<V> element() {
        return commands.element();
    }

    public void clear() {
        commands.clear();
    }

    public boolean isEmpty() {
        return commands.isEmpty();
    }

    public Iterator<ViewStateCommand<V>> iterator() {
        return commands.iterator();
    }

    public int size() {
        return commands.size();
    }
}
