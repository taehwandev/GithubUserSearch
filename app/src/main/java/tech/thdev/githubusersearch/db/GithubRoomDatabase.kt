package tech.thdev.githubusersearch.db

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import tech.thdev.githubusersearch.data.GithubUser

@Database(entities = [GithubUser::class], version = 1)
abstract class GithubRoomDatabase : RoomDatabase() {

    abstract fun githubUserDao(): GithubUserDao

    companion object {

        // For Singleton instantiation
        private var instance: GithubRoomDatabase? = null

        fun getInstance(application: Application) =
                instance ?: synchronized(this) {
                    instance ?: Room.databaseBuilder(application,
                            GithubRoomDatabase::class.java, "github.db").build()
                }
    }
}