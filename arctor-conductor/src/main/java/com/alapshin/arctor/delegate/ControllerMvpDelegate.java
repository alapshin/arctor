package com.alapshin.arctor.delegate;


import android.support.annotation.NonNull;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

public interface ControllerMvpDelegate<V extends MvpView, P extends Presenter<V>> {
    public void onCreateView(@NonNull V view, @NonNull P presenter);
    public void onDetach();
}
