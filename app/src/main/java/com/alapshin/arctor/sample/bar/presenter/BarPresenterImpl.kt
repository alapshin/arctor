package com.alapshin.arctor.sample.bar.presenter

import com.alapshin.arctor.presenter.rxjava2.RxPresenter
import com.alapshin.arctor.sample.bar.view.BarView

import javax.inject.Inject

class BarPresenterImpl @Inject
constructor() : RxPresenter<BarView>(), BarPresenter {
}
