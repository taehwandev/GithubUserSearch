package tech.thdev.githubusersearch.feature.github.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tech.thdev.githubusersearch.util.plusAssign

class SearchQueryViewModel : ViewModel() {

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