package com.alapshin.di;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class NonConfigurationInstance {
    AtomicLong nextId;
    Map<Long, Object> components;

    public NonConfigurationInstance(long seed) {
        nextId = new AtomicLong(seed);
        components = new HashMap<>();
    }
}
