package tech.thdev.githubusersearch.data.github.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import tech.thdev.githubusersearch.data.github.GitHubApi

@Module
@InstallIn(SingletonComponent::class)
class GitHubApiModule {

    @Provides
    fun provideGitHubApi(
        retrofit: Retrofit,
    ): GitHubApi =
        retrofit.create(GitHubApi::class.java)
}