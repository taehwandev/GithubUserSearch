package tech.thdev.githubusersearch

import android.app.Application
import android.content.Context

class GithubApp : Application() {

    companion object {
        private lateinit var instance: Application

        val context: Context
            get() = instance.applicationContext
    }

    init {
        instance = this
    }
}