package com.alapshin.arctor.presenter;


import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

public class PresenterBundleTest {
    @Test
    public void shouldBeEmpty() {
        PresenterBundle bundle = new PresenterBundle();
        assertThat(bundle.size()).isZero();
        assertThat(bundle.isEmpty()).isTrue();
    }

    @Test
    public void shouldBeNonEmpty() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        PresenterBundle bundle = new PresenterBundle(map);
        assertThat(bundle.size()).isNotZero();
        assertThat(bundle.isEmpty()).isFalse();
    }

    @Test
    public void shouldContainKey() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        PresenterBundle bundle = new PresenterBundle(map);
        assertThat(bundle.containsKey("foo")).isTrue();
    }

    @Test
    public void clearWorks() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        PresenterBundle bundle = new PresenterBundle(map);
        bundle.clear();
        assertThat(bundle.size()).isZero();
        assertThat(bundle.isEmpty()).isTrue();
    }

    @Test
    public void removeWorks() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        PresenterBundle bundle = new PresenterBundle(map);
        bundle.remove("foo");
        assertThat(bundle.containsKey("foo")).isFalse();
    }
}
