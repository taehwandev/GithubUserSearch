package tech.thdev.githubusersearch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.thdev.githubusersearch.database.model.GitHubUser

@Database(entities = [GitHubUser::class], version = 1)
abstract class GitHubDatabase : RoomDatabase() {

    abstract fun gitHubUserDao(): GitHubUserDao

    companion object {

        // For Singleton instantiation
        private var instance: GitHubDatabase? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    GitHubDatabase::class.java, "github.db"
                ).build()
            }
    }
}