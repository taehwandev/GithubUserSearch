package tech.thdev.githubusersearch.view.common.viewmodel

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import tech.thdev.githubusersearch.base.viewmodel.BaseLifecycleViewModel
import tech.thdev.githubusersearch.util.plusAssign

class SearchQueryViewModel : BaseLifecycleViewModel() {

    lateinit var updateSearchQuery: (String) -> Unit

    private val searchQuerySubject = BehaviorSubject.create<String>()

    init {
        Log.d("TEMP", "create!!!!!!!!!")
        disposables += searchQuerySubject
                .subscribeOn(Schedulers.io())
                .filter {
                    ::updateSearchQuery.isInitialized
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateSearchQuery(it)
                }, {})
    }

    fun setSearchQuery(query: String?) {
        searchQuerySubject.onNext(query ?: "")
    }
}