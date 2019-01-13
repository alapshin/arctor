package com.alapshin.arctor.sample.main.view;

import com.alapshin.arctor.view.MvpView;

public interface MainView extends MvpView {
    void onData(long data);
    void onError(Throwable error);
    void onProgress();
}
