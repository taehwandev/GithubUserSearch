//package tech.thdev.githubusersearch.feature.main.viewmodel
//
//import io.reactivex.android.plugins.RxAndroidPlugins
//import io.reactivex.schedulers.Schedulers
//import io.reactivex.subscribers.TestSubscriber
//import org.junit.Before
//import org.junit.Test
//import tech.thdev.githubusersearch.R
//import tech.thdev.githubusersearch.feature.main.FilterStatusViewModel
//
//class FilterStatusViewModelTest {
//
//    private lateinit var viewModel: FilterStatusViewModel
//
//    @Before
//    fun setUp() {
//        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
//
//        viewModel = FilterStatusViewModel()
//    }
//
//    @Test
//    fun filterChangeTest() {
//        val testSubscriber = TestSubscriber<Int>()
//        viewModel.onUpdateFilterIcon = {
//            testSubscriber.onNext(it)
//        }
//
//        viewModel.selectSortType(FilterStatusViewModel.FilterType.FILTER_SORT_NAME)
//
//        testSubscriber.awaitCount(1)
//        testSubscriber.assertValue(R.drawable.ic_sort_alphabet)
//    }
//}