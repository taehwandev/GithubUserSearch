package tech.thdev.githubusersearch.data.source.search

import io.reactivex.rxjava3.core.Single
import tech.thdev.githubusersearch.data.GitHubUser
import tech.thdev.githubusersearch.db.GitHubRoomDatabase
import tech.thdev.githubusersearch.db.GithubUserDao

class GitHubSearchLocalDataSource(githubRoomDatabase: GitHubRoomDatabase) {

    private val githubUserDao: GithubUserDao = githubRoomDatabase.githubUserDao()

    fun insertGithubUser(item: GitHubUser) {
        githubUserDao.insert(item)
    }

    fun getAllLikeUser() =
        githubUserDao.selectUser()

    fun searchUserLocal(name: String): Single<MutableList<GitHubUser>> =
        githubUserDao.searchUser(name)

    fun removeUser(login: String) {
        githubUserDao.deleteUser(login)
    }
}