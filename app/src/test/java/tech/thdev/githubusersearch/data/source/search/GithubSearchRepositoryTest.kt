package tech.thdev.githubusersearch.data.source.search

import io.reactivex.Single
import io.reactivex.functions.BiConsumer
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import tech.thdev.githubusersearch.data.GithubResponse
import tech.thdev.githubusersearch.network.GithubInterface
import tech.thdev.githubusersearch.network.RetrofitFactory
import tech.thdev.githubusersearch.util.NoNetworkException
import tech.thdev.githubusersearch.util.createRetrofit
import java.util.concurrent.TimeoutException


class GithubSearchRepositoryTest {

    private lateinit var api: GithubInterface
    private lateinit var githubSearchRepository: GithubSearchRepository

    private var isAvailableNetwork = true

    @Before
    fun setUp() {
        api = createRetrofit(GithubInterface::class.java, RetrofitFactory.baseUrl) {
            isAvailableNetwork
        }
        githubSearchRepository = GithubSearchRepository.getInstance(api)
    }

    @Test
    fun githubSearchTest() {
        val testSubscriber = TestSubscriber<Boolean>()
        githubSearchRepository.searchUser("taehwan", 0, 10)
                .subscribe { t1, t2 ->
                    when (t2) {
                        is NoNetworkException -> {

                        }
                        else -> {
                            testSubscriber.onNext(t1.totalCount == 1)
                        }
                    }
                }

        testSubscriber.assertValue(true)
    }

    @Test
    fun networkExceptionTest() {
        isAvailableNetwork = false

        val testSubscriber = TestSubscriber<Boolean>()

        githubSearchRepository.searchUser("taehwandev", 0, 10)
                .subscribe { t1, t2 ->
                    when (t2) {
                        is NoNetworkException -> {
                            testSubscriber.onNext(true)
                        }
                        else -> {
                            // success.
                        }
                    }
                }

        testSubscriber.assertValue(true)
    }

    class GithubBiConsumer<T>(private val onSuccess: (item: T) -> Unit,
                                          private val onError: (throwable: Throwable) -> Unit) : BiConsumer<T, Throwable> {

        override fun accept(item: T, throwable: Throwable) {
            when (throwable) {
                is NoNetworkException, is TimeoutException -> {
                    onError(throwable)
                }
                else -> {
                    onSuccess(item)
                }
            }
        }
    }
}