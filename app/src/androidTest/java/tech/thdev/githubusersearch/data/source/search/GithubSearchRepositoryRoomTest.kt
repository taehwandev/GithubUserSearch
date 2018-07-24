package tech.thdev.githubusersearch.data.source.search

import android.arch.persistence.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.thdev.githubusersearch.data.GithubUser
import tech.thdev.githubusersearch.db.GithubRoomDatabase
import tech.thdev.githubusersearch.network.GithubInterface
import tech.thdev.githubusersearch.network.RetrofitFactory
import tech.thdev.githubusersearch.util.createRetrofit


@RunWith(AndroidJUnit4::class)
class GithubSearchRepositoryRoomTest {

    private lateinit var api: GithubInterface
    private lateinit var githubSearchRepository: GithubSearchRepository

    private lateinit var database: GithubRoomDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, GithubRoomDatabase::class.java).build()

        api = createRetrofit(GithubInterface::class.java, RetrofitFactory.baseUrl) {
            true
        }
        githubSearchRepository = GithubSearchRepository.getInstance(api, database)
    }

    @Test
    fun likeItemTest() {
        val item = GithubUser("login", 0, "", 100.0)
        githubSearchRepository.likeUserInfo(item)

        val testSubscriber = TestSubscriber<String>()
        githubSearchRepository.getAllLocalData()
                .subscribe { t1, _ ->
                    testSubscriber.onNext(t1[0].login)
                }

        testSubscriber.assertValue("login")
    }

    @Test
    fun unLikeItemTest() {
        val list = mutableListOf(
                GithubUser("taehwan", 0, "", 100.0),
                GithubUser("taaa", 2, "", 100.0),
                GithubUser("aaa", 5, "", 100.0),
                GithubUser("abc", 7, "", 100.0))
        list.forEach {
            githubSearchRepository.likeUserInfo(it)
        }

        githubSearchRepository.unlikeUserInfo(list[2])

        val testSubscriber = TestSubscriber<Boolean>()
        githubSearchRepository.getAllLocalData()
                .subscribe { t1, _ ->
                    testSubscriber.onNext(t1.find { it.login == "aaa" } == null)
                }

        testSubscriber.assertValue(true)
    }
}