package tech.thdev.githubusersearch.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Single
import tech.thdev.githubusersearch.data.GithubUser

@Dao
interface GithubUserDao {

    @Insert
    fun insert(user: GithubUser)

    @Query("SELECT * FROM githubuser ORDER BY score DESC")
    fun selectUser(): Single<MutableList<GithubUser>>

    @Query("SELECT * FROM githubuser where login LIKE '%' || :login || '%' ORDER BY score DESC")
    fun searchUser(login: String): Single<MutableList<GithubUser>>

    @Query("DELETE FROM githubuser where login = :login")
    fun deleteUser(login: String)

    @Query("DELETE FROM githubuser")
    fun deleteAll()
}
