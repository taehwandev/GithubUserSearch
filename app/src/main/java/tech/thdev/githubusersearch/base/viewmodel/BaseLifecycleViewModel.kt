package tech.thdev.githubusersearch.base.viewmodel

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.disposables.CompositeDisposable

abstract class BaseLifecycleViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        Log.i("BaseLifecycleViewModel", "onCleared!!!")
        disposables.clear()
    }
}