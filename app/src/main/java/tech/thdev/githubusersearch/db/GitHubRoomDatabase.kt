package tech.thdev.githubusersearch.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.thdev.githubusersearch.data.GitHubUser

@Database(entities = [GitHubUser::class], version = 1)
abstract class GitHubRoomDatabase : RoomDatabase() {

    abstract fun githubUserDao(): GithubUserDao

    companion object {

        // For Singleton instantiation
        private var instance: GitHubRoomDatabase? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    application,
                    GitHubRoomDatabase::class.java, "github.db"
                ).build()
            }
    }
}