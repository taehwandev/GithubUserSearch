package tech.thdev.githubusersearch.common.viewmodel

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tech.thdev.githubusersearch.base.viewmodel.BaseLifecycleViewModel
import tech.thdev.githubusersearch.util.NoNetworkException
import tech.thdev.githubusersearch.util.plusAssign

abstract class BaseGitHubViewModel : BaseLifecycleViewModel() {

    lateinit var onShowProgress: () -> Unit

    lateinit var onHideProgress: () -> Unit

    lateinit var onShowNetworkError: () -> Unit

    lateinit var onShowOtherError: (message: String?) -> Unit

    protected val uiThreadSubject = BehaviorSubject.create<() -> Unit>()

    /**
     * 로딩 상태 저장
     */
    protected var isLoading = false

    /**
     * Pair<first, seconds>
     * first : background thread
     * seconds : ui thread
     */
    protected val databaseHelperSubject = BehaviorSubject.create<Pair<() -> Unit, () -> Unit>>()

    init {
        disposables += uiThreadSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it() }, {})

        disposables += databaseHelperSubject
            .observeOn(Schedulers.io())
            .map { (first, seconds) ->
                first()
                seconds
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it()
            }, {
                it.printStackTrace()
            })
    }

    protected fun <T : Any> progressCompose() =
        SingleTransformer<T, T> {
            it
                .doOnSubscribe {
                    isLoading = true

                    uiThreadSubject.onNext {
                        if (::onShowProgress.isInitialized) {
                            onShowProgress()
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    uiThreadSubject.onNext {
                        hideProgress()
                    }
                }
        }

    protected fun onErrorThrowable(error: Throwable) {
        isLoading = false

        when (error) {
            is NoNetworkException -> {
                uiThreadSubject.onNext {
                    hideProgress()

                    if (::onShowNetworkError.isInitialized) {
                        onShowNetworkError()
                    }
                }
            }

            else -> {
                uiThreadSubject.onNext {
                    hideProgress()

                    if (::onShowOtherError.isInitialized) {
                        onShowOtherError(error.message)
                    }
                }
            }
        }

        error.printStackTrace()
    }

    private fun hideProgress() {
        if (::onHideProgress.isInitialized) {
            onHideProgress()
        }
    }
}