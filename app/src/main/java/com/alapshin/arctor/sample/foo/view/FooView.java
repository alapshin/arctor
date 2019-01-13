package com.alapshin.arctor.sample.foo.view;

import com.alapshin.arctor.view.MvpView;

public interface FooView extends MvpView {
    void onProgress();
    void onData(long data);
    void onError(Throwable error);
}
