package tech.thdev.githubusersearch.feature.main.like

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import tech.thdev.githubusersearch.base.viewmodel.BaseRxViewModel
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.feature.main.model.convert.convert
import tech.thdev.githubusersearch.util.plusAssign

class LikeViewModel(
    private val gitHubSearchRepository: GitHubSearchRepository,
) : BaseRxViewModel() {

    private val _mainListUiState = MutableLiveData<MainListUiState>(MainListUiState.UserItems.Default)
    val mainListUiState: LiveData<MainListUiState> get() = _mainListUiState

    init {
        disposables += loadData()
            .subscribe()
    }

    @VisibleForTesting
    fun loadData(): Flowable<MainListUiState> =
        gitHubSearchRepository.observeLoadLikedData()
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.convert()
            }
            .doOnNext {
                _mainListUiState.value = it
            }
            .doOnError {
                it.printStackTrace()
            }
}