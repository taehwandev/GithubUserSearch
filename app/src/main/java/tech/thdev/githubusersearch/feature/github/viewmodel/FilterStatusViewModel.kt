package tech.thdev.githubusersearch.feature.github.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.util.plusAssign

class FilterStatusViewModel : ViewModel() {

    enum class FilterType {
        FILTER_SORT_DEFAULT, // default sort - best match
        FILTER_SORT_NAME, // sort name
        FILTER_SORT_DATE_OF_REGISTRATION // user id
    }

    lateinit var onUpdateFilterIcon: (icon: Int) -> Unit
    lateinit var updateFilterStatus: (filterType: FilterType) -> Unit

    private val filterStatusChangeSubject = BehaviorSubject.create<FilterType>()

    var prevFilterType: FilterType = FilterType.FILTER_SORT_DEFAULT
        private set

    init {
        disposables += filterStatusChangeSubject
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.also { prevFilterType = it }
            }
            .subscribe {
                if (::updateFilterStatus.isInitialized) {
                    updateFilterStatus(it)
                }

                if (::onUpdateFilterIcon.isInitialized) {
                    onUpdateFilterIcon(filterIcon)
                }
            }
    }

    private val filterIcon: Int
        get() = when (prevFilterType) {
            FilterType.FILTER_SORT_NAME -> R.drawable.ic_sort_alphabet
            FilterType.FILTER_SORT_DATE_OF_REGISTRATION -> R.drawable.ic_sort
            else -> R.drawable.ic_sort_numbers
        }

    fun selectFilter(filterType: FilterType) {
        filterStatusChangeSubject.onNext(filterType)
    }
}