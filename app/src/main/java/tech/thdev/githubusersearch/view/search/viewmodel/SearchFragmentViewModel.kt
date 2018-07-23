package tech.thdev.githubusersearch.view.search.viewmodel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import tech.thdev.githubusersearch.common.viewmodel.BaseGithubViewModel
import tech.thdev.githubusersearch.data.source.search.GithubSearchRepository
import tech.thdev.githubusersearch.util.plusAssign
import tech.thdev.githubusersearch.view.common.adapter.viewmodel.UserAdapterViewModel
import tech.thdev.githubusersearch.view.common.viewmodel.FilterStatusViewModel
import java.util.concurrent.TimeUnit

class SearchFragmentViewModel(private val adapterViewModel: UserAdapterViewModel,
                              private val githubSearchRepository: GithubSearchRepository) : BaseGithubViewModel() {

    private val searchQuerySubject = PublishSubject.create<String>()
    private val sortSubject = BehaviorSubject.create<FilterStatusViewModel.FilterType>()

    private var prevSelectFilterType = FilterStatusViewModel.FilterType.FILTER_SORT_DEFAULT
    private var prevSearchQuery: String = ""

    init {
        initSearchQuerySubject()

        disposables += sortSubject
                .subscribeOn(Schedulers.io())
                .map {
                    it.also { prevSelectFilterType = it }
                }
                .switchMapSingle { filterType ->
                    githubSearchRepository.getAllCacheItems(prevSearchQuery)
                            .map {
                                Pair(filterType, it)
                            }
                }
                .map { (filterType, userList) ->
                    when (filterType) {
                        FilterStatusViewModel.FilterType.FILTER_SORT_NAME -> {
                            userList.sortBy { it.login }
                        }
                        FilterStatusViewModel.FilterType.FILTER_SORT_DATE_OF_REGISTRATION -> {
                            userList.sortBy { it.id }
                        }
                    }
                    userList
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapterViewModel.run {
                        adapterRepository.clear()
                        adapterRepository.addItems(UserAdapterViewModel.VIEW_TYPE_ITEM, it)
                        notifyDataSetChanged()
                    }
                }, {})
    }

    fun initSearchQuerySubject() {
        disposables += searchQuerySubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter {
                    it.isNotEmpty()
                }
                .distinctUntilChanged()
                .map {
                    it.also { prevSearchQuery = it }
                }
                .switchMapSingle {
                    githubSearchRepository.searchUser(it, 0, 50)
                            .subscribeOn(Schedulers.io())
                            .compose(progressCompose())
                }
                .subscribe({
                    sortSubject.onNext(prevSelectFilterType)
                }, ::onErrorThrowable)
    }

    fun search(searchQuery: String = prevSearchQuery) {
        searchQuerySubject.onNext(searchQuery)
    }

    fun changeFilter(filter: FilterStatusViewModel.FilterType) {
        sortSubject.onNext(filter)
    }

    override fun onCleared() {
        super.onCleared()
        githubSearchRepository.clear()
    }
}