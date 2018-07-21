package tech.thdev.githubusersearch.data.source.search

import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import tech.thdev.githubusersearch.network.GithubInterface
import tech.thdev.githubusersearch.network.RetrofitFactory
import tech.thdev.githubusersearch.util.createRetrofit


class GithubSearchRepositoryTest {

    private lateinit var api: GithubInterface
    private lateinit var githubSearchRepository: GithubSearchRepository

    @Before
    fun setUp() {
        api = createRetrofit(GithubInterface::class.java, RetrofitFactory.baseUrl) {
            true
        }
        githubSearchRepository = GithubSearchRepository.getInstance(api)
    }

    @Test
    fun githubSearchTest() {
        val testSubscriber = TestSubscriber<Boolean>()
        githubSearchRepository.searchUser("taehwandev", 0, 10)
                .subscribe { t1, t2 ->
                    testSubscriber.onNext(t1.totalCount == 1)
                }

        testSubscriber.assertValue(true)
    }
}