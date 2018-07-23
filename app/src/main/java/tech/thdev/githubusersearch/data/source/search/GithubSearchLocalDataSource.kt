package tech.thdev.githubusersearch.data.source.search

import io.reactivex.Single
import tech.thdev.githubusersearch.data.GithubUser
import tech.thdev.githubusersearch.db.GithubRoomDatabase
import tech.thdev.githubusersearch.db.GithubUserDao

class GithubSearchLocalDataSource(githubRoomDatabase: GithubRoomDatabase) {

    private val githubUserDao: GithubUserDao = githubRoomDatabase.githubUserDao()

    fun insertGithubUser(item: GithubUser) {
        githubUserDao.insert(item)
    }

    fun getAllLikeUser() =
            githubUserDao.selectUser()

    fun searchUserLocal(name: String): Single<MutableList<GithubUser>> =
            githubUserDao.searchUser(name)

    fun removeUser(login: String) {
        githubUserDao.deleteUser(login)
    }
}