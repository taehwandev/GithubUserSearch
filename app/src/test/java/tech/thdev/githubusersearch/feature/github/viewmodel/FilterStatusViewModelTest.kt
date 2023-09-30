package tech.thdev.githubusersearch.feature.github.viewmodel

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import tech.thdev.githubusersearch.R

class FilterStatusViewModelTest {

    private lateinit var viewModel: FilterStatusViewModel

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        viewModel = FilterStatusViewModel()
    }

    @Test
    fun filterChangeTest() {
        val testSubscriber = TestSubscriber<Int>()
        viewModel.onUpdateFilterIcon = {
            testSubscriber.onNext(it)
        }

        viewModel.selectFilter(FilterStatusViewModel.FilterType.FILTER_SORT_NAME)

        testSubscriber.awaitCount(1)
        testSubscriber.assertValue(R.drawable.ic_sort_alphabet)
    }
}