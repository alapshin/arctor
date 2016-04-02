package com.alapshin.di;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class ComponentCacheDelegate {
    private static final String NEXT_ID_KEY = "next-presenter-id";

    private NonConfigurationInstance nonConfigurationInstance;

    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable Object nonConfigurationInstance) {
        this.nonConfigurationInstance = (NonConfigurationInstance) nonConfigurationInstance;
        if (this.nonConfigurationInstance == null) {
            long seed = savedInstanceState == null ? 0 : savedInstanceState.getLong(NEXT_ID_KEY);
            this.nonConfigurationInstance = new NonConfigurationInstance(seed);
        }
    }

    public Object onRetainCustomNonConfigurationInstance() {
        return nonConfigurationInstance;
    }

    public long generateId() {
        return nonConfigurationInstance.nextId.getAndIncrement();
    }

    @SuppressWarnings("unchecked")
    public final <C> C getComponent(long index) {
        return (C) nonConfigurationInstance.components.get(index);
    }

    public <C> void setComponent(long index, C component) {
        nonConfigurationInstance.components.put(index, component);
    }
}
