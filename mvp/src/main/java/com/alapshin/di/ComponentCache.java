package com.alapshin.di;

public interface ComponentCache {
    long generateComponentId();
    <C> C getComponent(long index);
    <C> void setComponent(long index, C component);
}
