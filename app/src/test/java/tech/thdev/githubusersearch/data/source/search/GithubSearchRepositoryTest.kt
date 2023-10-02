//package tech.thdev.githubusersearch.data.source.search
//
//import com.nhaarman.mockitokotlin2.mock
//import io.reactivex.subscribers.TestSubscriber
//import org.junit.Before
//import org.junit.Test
//import tech.thdev.githubusersearch.database.GitHubDatabase
//import tech.thdev.githubusersearch.network.RetrofitFactory
//import tech.thdev.githubusersearch.util.NoNetworkException
//import tech.thdev.githubusersearch.util.createRetrofit
//
//class GithubSearchRepositoryTest {
//
//    private lateinit var api: GitHubInterface
//    private lateinit var githubSearchRepository: GitHubSearchRepository
//
//    private var isAvailableNetwork = true
//
//    private val database: GitHubDatabase = mock()
//
//    @Before
//    fun setUp() {
//        api = createRetrofit(GitHubInterface::class.java, RetrofitFactory.baseUrl) {
//            isAvailableNetwork
//        }
//        githubSearchRepository = GitHubSearchRepository.getInstance(api, database)
//    }
//
//    @Test
//    fun githubSearchTest() {
//        val testSubscriber = TestSubscriber<Boolean>()
//        githubSearchRepository.searchUser("taehwan", 1)
//                .subscribe { t1, t2 ->
//                    when (t2) {
//                        is NoNetworkException -> {
//
//                        }
//                        else -> {
//                            testSubscriber.onNext(t1.size == 1)
//                        }
//                    }
//                }
//
//        testSubscriber.assertValue(true)
//    }
//
//    @Test
//    fun networkExceptionTest() {
//        isAvailableNetwork = false
//
//        val testSubscriber = TestSubscriber<Boolean>()
//
//        githubSearchRepository.searchUser("taehwandev", 1)
//                .subscribe { _, t2 ->
//                    when (t2) {
//                        is NoNetworkException -> {
//                            testSubscriber.onNext(true)
//                        }
//                        else -> {
//                            // success.
//                        }
//                    }
//                }
//
//        testSubscriber.assertValue(true)
//    }
//}