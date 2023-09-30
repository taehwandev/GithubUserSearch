package tech.thdev.githubusersearch.feature.github.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.util.plusAssign
import tech.thdev.githubusersearch.feature.github.GithubUserFragment
import tech.thdev.githubusersearch.feature.github.adapter.viewmodel.UserAdapterViewModel
import java.util.concurrent.TimeUnit

class GitHubUserFragmentViewModel(
    private val viewType: Int,
    private val adapterViewModel: UserAdapterViewModel,
    private val gitHubSearchRepository: GitHubSearchRepository,
) : ViewModel() {

    /**
     * Search Subject
     */
    private val searchQuerySubject = PublishSubject.create<String>()

    private val loadMoreSubject = PublishSubject.create<Boolean>()

    /**
     * FilterType and result data.
     */
    private val dataSortSubject = BehaviorSubject.create<Triple<String, FilterStatusViewModel.FilterType, MutableList<GitHubUser>>>()

    /**
     * Remote Cache or Local Room 데이터를 불러온다
     */
    private val getCacheDataSubject = BehaviorSubject.create<Pair<String, FilterStatusViewModel.FilterType>>()

    private var prevSelectFilterType = FilterStatusViewModel.FilterType.FILTER_SORT_DEFAULT
    private var prevSearchQuery: String = ""

    lateinit var noSearchItem: () -> Unit
    lateinit var showEmptyView: () -> Unit
    lateinit var hideEmptyView: () -> Unit

    private val TAG = this.javaClass.simpleName
    private val perPage = 50

    init {
        Log.d(TAG, "Create viewType!!!! $viewType")

        initSearchQuerySubject()
        adapterViewModel.init()

        disposables += dataSortSubject
            .subscribeOn(Schedulers.io())
            .map {
                it.also {
                    prevSearchQuery = it.first
                    prevSelectFilterType = it.second
                }
            }
            .filter {
                uiThreadSubject.onNext {
                    if (it.third.size == 0) {
                        adapterViewModel.run {
                            adapterRepository.clear()
                            notifyDataSetChanged()
                        }
                        if (it.first.isNotEmpty()) {
                            if (::noSearchItem.isInitialized) {
                                noSearchItem()
                            }
                        } else {
                            if (::noSearchItem.isInitialized) {
                                showEmptyView()
                            }
                        }
                    } else {
                        if (::hideEmptyView.isInitialized) {
                            hideEmptyView()
                        }
                    }
                }
                Log.i("TEMP", "it? ${it.third.size}")
                it.third.size > 0
            }
            .map { (_, filterType, userList) ->
                when (filterType) {
                    FilterStatusViewModel.FilterType.FILTER_SORT_NAME -> {
                        userList.sortBy { it.login }
                    }

                    FilterStatusViewModel.FilterType.FILTER_SORT_DATE_OF_REGISTRATION -> {
                        userList.sortBy { it.id }
                    }

                    else -> {
                        // Do nothing.
                    }
                }
                Pair(filterType, userList)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map { (filterType, userList) ->
                adapterViewModel.run {
                    adapterRepository.clear()

                    when (filterType) {
                        FilterStatusViewModel.FilterType.FILTER_SORT_NAME -> {
                            var prevStart = ""
                            userList.forEach {
                                val nowStart: String = it.login.substring(0, 1)
                                if (prevStart != nowStart) {
                                    adapterRepository.addItem(UserAdapterViewModel.VIEW_TYPE_SECTION, nowStart)
                                }
                                adapterRepository.addItem(UserAdapterViewModel.VIEW_TYPE_ITEM, it)
                                prevStart = nowStart
                            }
                        }

                        else -> {
                            Log.d("TEMP", "addItems? $userList")
                            adapterRepository.addItems(UserAdapterViewModel.VIEW_TYPE_ITEM, userList)
                        }
                    }
                }
            }
            .subscribe({
                isLoading = false

                Log.d("TEMP", "notifyDataSetChanged")
                Log.d("TEMP", "adapterViewModel ${adapterViewModel.hashCode()}")
                adapterViewModel.notifyDataSetChanged()
            }, {
                it.printStackTrace()
            })

        disposables += getCacheDataSubject
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .switchMapSingle { (searchQuery, filterType) ->
                Log.i(TAG, "load data searchQuery $searchQuery")
                when (viewType) {
                    GithubUserFragment.VIEW_TYPE_SEARCH -> {
                        githubSearchRepository.getAllCacheItems(searchQuery)
                    }

                    else -> {
                        getLocalData(searchQuery)
                    }
                }.map {
                    Triple(searchQuery, filterType, it)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                dataSortSubject.onNext(it)
            }

        disposables += loadMoreSubject
            .observeOn(Schedulers.io())
            .filter {
                it
            }
            .switchMapSingle {
                githubSearchRepository.searchUser(prevSearchQuery, perPage)
                    .observeOn(Schedulers.io())
                    .compose(progressCompose())
                    .map { Triple(prevSearchQuery, prevSelectFilterType, it) }
            }
            .subscribe({
                dataSortSubject.onNext(it)
            }, ::onErrorThrowable)
    }

    private fun getLocalData(searchQuery: String) =
        if (searchQuery.isNotEmpty()) {
            githubSearchRepository.searchUserLocal(searchQuery)
        } else {
            githubSearchRepository.getAllLocalData()
        }

    private fun UserAdapterViewModel.init() {
        onLikeUserInfo = { adapterPosition, item ->
            databaseHelperSubject.onNext(Pair({
                item.isLike = true
                githubSearchRepository.likeUserInfo(item)
                Unit
            }, {
                when (viewType) {
                    GithubUserFragment.VIEW_TYPE_SEARCH -> {
                        notifyItemChanged(adapterPosition)
                    }
                }
                Unit
            }))
        }

        onUnlikeUserInfo = { adapterPosition, item ->
            databaseHelperSubject.onNext(Pair({
                item.isLike = false
                githubSearchRepository.unlikeUserInfo(item)
                Unit
            }, {
                when (viewType) {
                    GithubUserFragment.VIEW_TYPE_SEARCH -> {
                        notifyItemChanged(adapterPosition)
                    }

                    GithubUserFragment.VIEW_TYPE_LIKED -> {
                        adapterRepository.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                    }
                }
                Unit
            }))
        }
    }

    fun initSearchQuerySubject() {
        disposables += searchQuerySubject
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMapSingle { searchQuery ->
                when (viewType) {
                    GithubUserFragment.VIEW_TYPE_SEARCH -> {
                        if (searchQuery.isNotEmpty()) {
                            githubSearchRepository.searchUser(searchQuery, perPage)
                        } else {
                            Single.just(mutableListOf())
                        }
                    }

                    else -> {
                        getLocalData(searchQuery)
                    }
                }.subscribeOn(Schedulers.io())
                    .compose(progressCompose())
                    .map { Triple(searchQuery, prevSelectFilterType, it) }
            }
            .subscribe({
                dataSortSubject.onNext(it)
            }, ::onErrorThrowable)
    }

    fun search(searchQuery: String) {
        searchQuerySubject.onNext(searchQuery)
    }

    fun changeFilter(filter: FilterStatusViewModel.FilterType) {
        getCacheDataSubject.onNext(Pair(prevSearchQuery, filter))
    }

    /**
     * Load
     *
     * 마지막 데이터로 검색 하고, 불러온다
     */
    fun loadGithubUser(searchQuery: String, filter: FilterStatusViewModel.FilterType) {
        getCacheDataSubject.onNext(Pair(searchQuery, filter))
    }

    fun loadMore(visibleItemCount: Int, totalItemCount: Int, firstVisibleItem: Int) {
        if (!isLoading && (firstVisibleItem + visibleItemCount) >= totalItemCount - 10) {
            loadMoreSubject.onNext(true)
        }
    }

    override fun onCleared() {
        gitHubSearchRepository.clear()
    }

    companion object {

        fun factory(
            viewType: Int,
            adapterViewModel: UserAdapterViewModel,
            gitHubSearchRepository: GitHubSearchRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GitHubUserFragmentViewModel(
                    viewType = viewType,
                    adapterViewModel = adapterViewModel,
                    gitHubSearchRepository = gitHubSearchRepository,
                ) as T
            }
        }
    }
}