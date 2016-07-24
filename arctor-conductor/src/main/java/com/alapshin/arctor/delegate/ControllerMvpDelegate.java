package com.alapshin.arctor.delegate;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alapshin.arctor.presenter.Presenter;
import com.alapshin.arctor.view.MvpView;

public interface ControllerMvpDelegate<V extends MvpView, P extends Presenter<V>> {
    void onCreate(@Nullable Bundle savedInstanceState);
    void onCreateView();
    void onAttach();
    void onDetach();
    void onDestroyView();
    void onDestroy();
    void onSaveInstanceState(Bundle outState);
}
