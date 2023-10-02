package tech.thdev.githubusersearch.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import tech.thdev.githubusersearch.database.model.GitHubUser

@Dao
interface GitHubUserDao {

    @Insert
    fun insert(user: GitHubUser)

    @Query("SELECT * FROM gitHubUser ORDER BY score DESC")
    fun allLiked(): Flowable<List<GitHubUser>>

    @Query("SELECT * FROM gitHubUser where login LIKE '%' || :login || '%' ORDER BY score DESC")
    fun searchLiked(login: String): Flowable<List<GitHubUser>>

    @Query("DELETE FROM gitHubUser where id = :id")
    fun deleteUser(id: Int)
}