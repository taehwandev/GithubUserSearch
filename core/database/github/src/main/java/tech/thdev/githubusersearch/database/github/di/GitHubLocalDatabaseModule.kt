package tech.thdev.githubusersearch.database.github.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import tech.thdev.githubusersearch.database.github.GitHubDatabase
import tech.thdev.githubusersearch.database.github.api.GitHubUserDao

@Module
@InstallIn(SingletonComponent::class)
object GitHubLocalDatabaseModule {

    @Singleton
    @Provides
    fun provideGitHubDatabase(
        @ApplicationContext context: Context
    ): GitHubDatabase {
        return GitHubDatabase.getInstance(context)
    }

    @Provides
    fun provideGitHubUserDao(gitHubDatabase: GitHubDatabase): GitHubUserDao {
        return gitHubDatabase.gitHubUserDao()
    }
}