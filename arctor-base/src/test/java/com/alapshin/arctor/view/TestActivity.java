package com.alapshin.arctor.view;


import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.LinearLayout;

import com.alapshin.arctor.R;

public class TestActivity extends MvpActivity<TestView, TestPresenter> implements TestView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(new LinearLayout(this));
    }
}
