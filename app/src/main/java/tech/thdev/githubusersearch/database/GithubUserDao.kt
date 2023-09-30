package tech.thdev.githubusersearch.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import tech.thdev.githubusersearch.database.model.GitHubUserEntity

@Dao
interface GithubUserDao {

    @Insert
    suspend fun insert(user: GitHubUserEntity)

    @Query("SELECT * FROM githubuser ORDER BY score DESC")
    suspend fun selectUser(): List<GitHubUserEntity>

    @Query("SELECT * FROM githubuser where login LIKE '%' || :login || '%' ORDER BY score DESC")
    suspend fun searchUser(login: String): List<GitHubUserEntity>

    @Query("DELETE FROM githubuser where login = :login")
    suspend fun deleteUser(login: String)

    @Query("DELETE FROM githubuser")
    suspend fun deleteAll()
}
