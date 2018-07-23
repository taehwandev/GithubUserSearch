package tech.thdev.githubusersearch.view.github.viewmodel

import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import tech.thdev.githubusersearch.common.viewmodel.BaseGithubViewModel
import tech.thdev.githubusersearch.data.GithubUser
import tech.thdev.githubusersearch.data.source.search.GithubSearchRepository
import tech.thdev.githubusersearch.util.plusAssign
import tech.thdev.githubusersearch.view.common.adapter.viewmodel.UserAdapterViewModel
import tech.thdev.githubusersearch.view.common.viewmodel.FilterStatusViewModel
import tech.thdev.githubusersearch.view.github.GithubUserFragment
import java.util.concurrent.TimeUnit

class GithubUserFragmentViewModel(private val viewType: Int,
                                  private val adapterViewModel: UserAdapterViewModel,
                                  private val githubSearchRepository: GithubSearchRepository) : BaseGithubViewModel() {

    /**
     * Search Subject
     */
    private val searchQuerySubject = PublishSubject.create<String>()

    private val loadMoreSubject = PublishSubject.create<Boolean>()

    /**
     * FilterType and result data.
     */
    private val dataSortSubject = BehaviorSubject.create<Triple<String, FilterStatusViewModel.FilterType, MutableList<GithubUser>>>()

    /**
     * Remote Cache or Local Room 데이터를 불러온다
     */
    private val getCacheDataSubject = BehaviorSubject.create<Pair<String, FilterStatusViewModel.FilterType>>()

    private var prevSelectFilterType = FilterStatusViewModel.FilterType.FILTER_SORT_DEFAULT
    private var prevSearchQuery: String = ""

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
                    }
                    userList
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isLoading = false

                    adapterViewModel.run {
                        adapterRepository.clear()
                        adapterRepository.addItems(UserAdapterViewModel.VIEW_TYPE_ITEM, it)
                        notifyDataSetChanged()
                    }
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
                Log.e("TEMP", "adapterPosition $adapterPosition")
                when (viewType) {
                    GithubUserFragment.VIEW_TYPE_SEARCH -> {
                        notifyItemChanged(adapterPosition)
                    }
                    GithubUserFragment.VIEW_TYPE_LIKED -> {
                        adapterRepository.addItem(UserAdapterViewModel.VIEW_TYPE_ITEM, item)
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
                Log.e("TEMP", "adapterPosition $adapterPosition")
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
}