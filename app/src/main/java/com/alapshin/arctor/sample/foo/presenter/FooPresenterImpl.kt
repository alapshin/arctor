package com.alapshin.arctor.sample.foo.presenter

import android.util.Log
import com.alapshin.arctor.presenter.PresenterBundle
import com.alapshin.arctor.presenter.rxjava2.RxPresenter
import com.alapshin.arctor.sample.foo.view.FooView

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FooPresenterImpl @Inject
constructor() : RxPresenter<FooView>(), FooPresenter {
    val TAG = FooPresenterImpl::class.java.simpleName

    data class State(
            val data: Long? = null,
            val error: Throwable? = null,
            val progress: Boolean = false)

    override fun onCreate(bundle: PresenterBundle?) {
        super.onCreate(bundle)
        if (bundle == null) {
            addDisposable(Observable.interval(5, 1, TimeUnit.SECONDS)
                    .map { State(data = it) }
                    .startWith(State(progress = true))
                    .onErrorReturn { State(error = it) }
                    .compose(waitViewLatest())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { state ->
                        if (state.progress) {
                            view.onProgress()
                        } else if (state.error != null) {
                            Log.d(TAG, "Error", state.error)
                            view.onError(state.error)
                        } else if (state.data != null) {
                            view.onData(state.data)
                        }
                    }
            )
        }
    }
}
