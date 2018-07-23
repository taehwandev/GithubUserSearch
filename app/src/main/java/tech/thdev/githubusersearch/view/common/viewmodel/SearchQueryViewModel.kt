package tech.thdev.githubusersearch.view.common.viewmodel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import tech.thdev.githubusersearch.base.viewmodel.BaseLifecycleViewModel
import tech.thdev.githubusersearch.util.plusAssign

class SearchQueryViewModel : BaseLifecycleViewModel() {

    lateinit var updateSearchQuery: (String) -> Unit
    lateinit var updateSearchQueryCommit: (String) -> Unit

    private val searchQuerySubject = BehaviorSubject.create<String>()

    var prevSearchQuery: String = ""
        private set

    init {
        disposables += searchQuerySubject
                .subscribeOn(Schedulers.io())
                .map {
                    it.also { prevSearchQuery = it }
                }
                .filter {
                    ::updateSearchQuery.isInitialized
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateSearchQuery(it)
                }, {})
    }

    fun setSearchQuery(query: String?, summit: Boolean = false) {
        searchQuerySubject.onNext(query ?: "")
        if (summit && ::updateSearchQueryCommit.isInitialized) {
            updateSearchQueryCommit(query ?: "")
        }
    }
}