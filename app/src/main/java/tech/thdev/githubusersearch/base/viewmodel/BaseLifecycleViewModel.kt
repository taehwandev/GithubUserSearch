package tech.thdev.githubusersearch.base.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseLifecycleViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        Log.i("BaseLifecycleViewModel", "onCleared!!!")
        disposables.clear()
    }
}