package tech.thdev.githubusersearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import tech.thdev.githubusersearch.data.GitHubUser

@Dao
interface GithubUserDao {

    @Insert
    fun insert(user: GitHubUser)

    @Query("SELECT * FROM githubuser ORDER BY score DESC")
    fun selectUser(): Single<MutableList<GitHubUser>>

    @Query("SELECT * FROM githubuser where login LIKE '%' || :login || '%' ORDER BY score DESC")
    fun searchUser(login: String): Single<MutableList<GitHubUser>>

    @Query("DELETE FROM githubuser where login = :login")
    fun deleteUser(login: String)

    @Query("DELETE FROM githubuser")
    fun deleteAll()
}
