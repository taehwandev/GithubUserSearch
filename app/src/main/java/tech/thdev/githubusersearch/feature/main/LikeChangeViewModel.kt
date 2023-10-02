package tech.thdev.githubusersearch.feature.main

import androidx.annotation.VisibleForTesting
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import tech.thdev.githubusersearch.base.viewmodel.BaseRxViewModel
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.util.plusAssign

class LikeChangeViewModel(
    private val gitHubSearchRepository: GitHubSearchRepository,
) : BaseRxViewModel() {


    @VisibleForTesting
    val change = PublishSubject.create<MainListUiState.UserItems.Info>()

    init {
        disposables += observeChange()
            .subscribe()
    }

    @VisibleForTesting
    fun observeChange(): Observable<MainListUiState.UserItems.Info> =
        change
            .observeOn(Schedulers.io())
            .doOnNext { item ->
                if (item.isLike) {
                    gitHubSearchRepository.unlikeUserInfo(id = item.id)
                } else {
                    gitHubSearchRepository.likeUserInfo(
                        GitHubUserEntity(
                            id = item.id,
                            login = item.login,
                            avatarUrl = item.avatarUrl,
                            score = item.score,
                            isLike = false,
                        )
                    )
                }
            }
            .doOnError { it.printStackTrace() }

    fun selectedLikeChange(item: MainListUiState.UserItems.Info) {
        change.onNext(item)
    }
}