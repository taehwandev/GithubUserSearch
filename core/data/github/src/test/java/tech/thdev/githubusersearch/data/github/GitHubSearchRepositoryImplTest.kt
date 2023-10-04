package tech.thdev.githubusersearch.data.github

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.thdev.githubusersearch.data.github.model.GitHubUserInfoResponse
import tech.thdev.githubusersearch.data.github.model.GitHubUserResponse
import tech.thdev.githubusersearch.database.github.api.GitHubUserDao
import tech.thdev.githubusersearch.database.github.api.model.GitHubUser
import tech.thdev.githubusersearch.domain.github.model.GitHubSortType
import tech.thdev.githubusersearch.domain.github.model.GitHubUserEntity

@OptIn(ExperimentalCoroutinesApi::class)
class GitHubSearchRepositoryImplTest {

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private val gitHubApi = mock<GitHubApi>()
    private val gitHubUserDao = mock<GitHubUserDao>()

    private val repository = GitHubSearchRepositoryImpl(
        gitHubApi = gitHubApi,
        gitHubUserDao = gitHubUserDao,
    )

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }


    @Test
    fun `test initData`() {
        Assertions.assertEquals(GitHubSortType.FILTER_SORT_DEFAULT, repository.sortList.value)
        Assertions.assertTrue(repository.cacheList.isEmpty())
        Assertions.assertEquals("", repository.cacheSearchKeyword)
        Assertions.assertEquals(1, repository.page)
        Assertions.assertFalse(repository.endPage)
    }

    @Test
    fun `test flowLoadData`() = runTest {
        val keyword = "new"

        val mockResponse = GitHubUserResponse(
            incompleteResults = true,
            items = listOf(
                GitHubUserInfoResponse(
                    login = "abc",
                ),
            )
        )
        whenever(gitHubApi.searchUser(searchKeyword = "new", page = 1, perPage = 30)).thenReturn(mockResponse)
        whenever(gitHubUserDao.flowLiked()).thenReturn(flowOf(emptyList()))

        repository.flowLoadData(keyword, 30)
            .test {
                val convert = listOf(
                    GitHubUserEntity.Default.copy(
                        login = "abc",
                    ),
                )
                Assertions.assertEquals(convert, awaitItem())
                Assertions.assertEquals(mockResponse.items, repository.cacheList)
                Assertions.assertEquals(keyword, repository.cacheSearchKeyword)
                Assertions.assertEquals(1, repository.page)
                Assertions.assertTrue(repository.endPage)

                verify(gitHubApi).searchUser(searchKeyword = "new", page = 1, perPage = 30)
                verify(gitHubUserDao).flowLiked()

                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test flowLoadData - liked`() = runTest {
        val keyword = "new"

        val mockResponse = GitHubUserResponse(
            incompleteResults = true,
            items = listOf(
                GitHubUserInfoResponse(
                    id = 12,
                    login = "abc",
                ),
            )
        )
        whenever(gitHubApi.searchUser(searchKeyword = "new", page = 1, perPage = 30)).thenReturn(mockResponse)
        val mockLikedList = listOf(
            GitHubUser(
                id = 12,
                login = "abc",
                avatarUrl = "",
                score = 0.0,
            )
        )
        whenever(gitHubUserDao.flowLiked()).thenReturn(flowOf(mockLikedList))

        repository.flowLoadData(keyword, 30)
            .test {
                val convert = listOf(
                    GitHubUserEntity.Default.copy(
                        id = 12,
                        login = "abc",
                        isLike = true,
                    ),
                )
                Assertions.assertEquals(convert, awaitItem())
                Assertions.assertEquals(mockResponse.items, repository.cacheList)
                Assertions.assertEquals(keyword, repository.cacheSearchKeyword)
                Assertions.assertEquals(1, repository.page)
                Assertions.assertTrue(repository.endPage)

                verify(gitHubApi).searchUser(searchKeyword = "new", page = 1, perPage = 30)
                verify(gitHubUserDao).flowLiked()

                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test flowLoadData - sorted`() = runTest {
        val keyword = "new"

        repository.sortList.value = GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION

        val mockResponse = GitHubUserResponse(
            incompleteResults = true,
            items = listOf(
                GitHubUserInfoResponse(
                    id = 22,
                    login = "bbb",
                ),
                GitHubUserInfoResponse(
                    id = 12,
                    login = "abc",
                ),
            )
        )
        whenever(gitHubApi.searchUser(searchKeyword = "new", page = 1, perPage = 30)).thenReturn(mockResponse)
        val mockLikedList = listOf(
            GitHubUser(
                id = 12,
                login = "abc",
                avatarUrl = "",
                score = 0.0,
            )
        )
        whenever(gitHubUserDao.flowLiked()).thenReturn(flowOf(mockLikedList))

        repository.flowLoadData(keyword, 30)
            .test {
                val convert = listOf(
                    GitHubUserEntity.Default.copy(
                        id = 12,
                        login = "abc",
                        isLike = true,
                    ),
                    GitHubUserEntity.Default.copy(
                        id = 22,
                        login = "bbb",
                        isLike = false,
                    ),
                )
                Assertions.assertEquals(convert, awaitItem())
                Assertions.assertEquals(mockResponse.items, repository.cacheList)
                Assertions.assertEquals(keyword, repository.cacheSearchKeyword)
                Assertions.assertEquals(1, repository.page)
                Assertions.assertTrue(repository.endPage)

                verify(gitHubApi).searchUser(searchKeyword = "new", page = 1, perPage = 30)
                verify(gitHubUserDao).flowLiked()

                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test flowLoadLikedData`() = runTest {
        val mockItem = listOf(
            GitHubUser(
                id = 0,
                login = "abc",
                avatarUrl = "",
                score = 0.0,
            )
        )
        whenever(gitHubUserDao.flowLiked()).thenReturn(flowOf(mockItem))

        repository.flowLoadLikedData()
            .test {
                val convert = listOf(
                    GitHubUserEntity.Default.copy(
                        login = "abc",
                        isLike = true,
                    ),
                )
                Assertions.assertEquals(convert, awaitItem())

                verify(gitHubUserDao).flowLiked()

                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test sortList`() {
        repository.sortList(GitHubSortType.FILTER_SORT_NAME)
        Assertions.assertEquals(GitHubSortType.FILTER_SORT_NAME, repository.sortList.value)
    }

    @Test
    fun `test likeUserInfo`() = runTest {
        val mockItem = GitHubUserEntity.Default.copy(id = 12, login = "aaa")
        repository.likeUserInfo(mockItem)
        verify(gitHubUserDao).insert(
            GitHubUser(
                id = 12,
                login = "aaa",
                avatarUrl = "",
                score = 0.0,
            )
        )
    }

    @Test
    fun `test unlikeUserInfo`() = runTest {
        repository.unlikeUserInfo(12)
        verify(gitHubUserDao).deleteUser(12)
    }
}