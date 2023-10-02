package tech.thdev.githubusersearch.feature.main.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import tech.thdev.githubusersearch.base.viewmodel.BaseRxViewModel
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.feature.main.model.convert.convert
import tech.thdev.githubusersearch.util.plusAssign


class SearchViewModel(
    private val gitHubSearchRepository: GitHubSearchRepository,
    isTest: Boolean = false,
) : BaseRxViewModel() {

    private val _showProgress = MutableLiveData(false)
    val showProgress: LiveData<Boolean> get() = _showProgress

    private val _mainListUiState = MutableLiveData<MainListUiState>(MainListUiState.UserItems.Default)
    val mainListUiState: LiveData<MainListUiState> get() = _mainListUiState

    @VisibleForTesting
    var loadData = false

    @VisibleForTesting
    var cacheKeyword = ""

    @VisibleForTesting
    val searchKeyword = PublishSubject.create<String>()

    init {
        if (isTest.not()) {
            disposables += observeData()
                .subscribe()

            disposables += observeLoadData()
                .subscribe()
        }
    }

    @VisibleForTesting
    fun observeData(): Flowable<MainListUiState> =
        gitHubSearchRepository.observeData()
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.convert()
            }
            .doOnNext {
                _mainListUiState.value = it
                _showProgress.value = false
            }
            .doOnError {
                it.printStackTrace()
            }

    @VisibleForTesting
    fun observeLoadData(): Observable<Boolean> =
        searchKeyword
            .filter { it.isNotEmpty() }
            .doOnNext {
                cacheKeyword = it
            }
            .switchMap {
                gitHubSearchRepository.loadData(it, 30)
                    .toObservable()
            }
            .doOnError {
                it.printStackTrace()
            }

    fun updateKeyword(keyword: String) {
        loadData = true
        searchKeyword.onNext(keyword)
    }

    fun changeSort(sort: GitHubSortType) {
        gitHubSearchRepository.sortList(sort)
    }

    fun loadMore(visibleItemCount: Int, totalItemCount: Int, firstVisibleItem: Int) {
        if (loadData.not() && (firstVisibleItem + visibleItemCount) >= totalItemCount - 10) {
            searchKeyword.onNext(cacheKeyword)
        }
    }

    override fun onCleared() {
        gitHubSearchRepository.clear()
    }
}