package tech.thdev.githubusersearch.database.github.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tech.thdev.githubusersearch.database.github.api.model.GitHubUser

@Dao
interface GitHubUserDao {

    @Insert
    suspend fun insert(user: GitHubUser)

    @Query("SELECT * FROM gitHubUser ORDER BY score DESC")
    fun flowLiked(): Flow<List<GitHubUser>>

    @Query("SELECT * FROM gitHubUser where login LIKE '%' || :login || '%' ORDER BY score DESC")
    fun flowSearchUser(login: String): Flow<List<GitHubUser>>

    @Query("DELETE FROM gitHubUser where id = :id")
    suspend fun deleteUser(id: Int)
}