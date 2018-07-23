package tech.thdev.githubusersearch.view.search.viewmodel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import tech.thdev.githubusersearch.common.viewmodel.BaseGithubViewModel
import tech.thdev.githubusersearch.data.source.search.GithubSearchRepository
import tech.thdev.githubusersearch.util.plusAssign
import tech.thdev.githubusersearch.view.common.adapter.viewmodel.UserAdapterViewModel
import java.util.concurrent.TimeUnit

class SearchFragmentViewModel(private val adapterViewModel: UserAdapterViewModel,
                              private val githubSearchRepository: GithubSearchRepository) : BaseGithubViewModel() {

    private val searchQuerySubject = PublishSubject.create<String>()
    private var lastSearchQuery: String = ""

    init {
        initSearchQuerySubject()
    }

    fun initSearchQuerySubject() {
        disposables += searchQuerySubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter {
                    it.isNotEmpty()
                }
                .distinctUntilChanged()
                .map {
                    it.also { lastSearchQuery = it }
                }
                .switchMapSingle {
                    githubSearchRepository.searchUser(it, 0, 50)
                            .subscribeOn(Schedulers.io())
                            .compose(progressCompose())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapterViewModel.run {
                        adapterRepository.addItems(UserAdapterViewModel.VIEW_TYPE_ITEM, it.items)
                        notifyDataSetChanged()
                    }
                }, ::onErrorThrowable)
    }

    fun search(searchQuery: String = lastSearchQuery) {
        searchQuerySubject.onNext(searchQuery)
    }
}