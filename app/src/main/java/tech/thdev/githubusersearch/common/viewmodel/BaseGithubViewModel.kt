package tech.thdev.githubusersearch.common.viewmodel

import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import tech.thdev.githubusersearch.base.viewmodel.BaseLifecycleViewModel
import tech.thdev.githubusersearch.util.NoNetworkException
import tech.thdev.githubusersearch.util.plusAssign

abstract class BaseGithubViewModel : BaseLifecycleViewModel() {

    lateinit var onShowProgress: () -> Unit

    lateinit var onHideProgress: () -> Unit

    lateinit var onShowNetworkError: () -> Unit

    lateinit var onShowOtherError: (message: String?) -> Unit

    protected val uiThreadSubject = BehaviorSubject.create<() -> Unit>()

    init {
        disposables += uiThreadSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it() }, {})
    }

    protected fun <T> progressCompose() =
            SingleTransformer<T, T> {
                it
                        .doOnSubscribe {
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
    }

    private fun hideProgress() {
        if (::onHideProgress.isInitialized) {
            onHideProgress()
        }
    }
}