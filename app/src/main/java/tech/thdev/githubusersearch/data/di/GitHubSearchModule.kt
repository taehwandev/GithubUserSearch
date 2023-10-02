package tech.thdev.githubusersearch.data.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tech.thdev.githubusersearch.data.GitHubSearchRepositoryImpl
import tech.thdev.githubusersearch.domain.GitHubSearchRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class GitHubSearchModule {

    @Binds
    @Reusable
    abstract fun bindGitHubSearchRepository(
        gitHubSearchRepository: GitHubSearchRepositoryImpl,
    ): GitHubSearchRepository
}