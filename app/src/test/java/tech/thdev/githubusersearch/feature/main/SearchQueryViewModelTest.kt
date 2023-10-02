package tech.thdev.githubusersearch.feature.main

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SearchQueryViewModelTest {

    private val viewModel = SearchQueryViewModel()

    @Test
    fun `test searchQuery`() = runTest {
        viewModel.searchQuery
            .test {
                Assertions.assertEquals("", awaitItem())

                viewModel.setSearchQuery("query")
                Assertions.assertEquals("query", awaitItem())

                this.cancelAndConsumeRemainingEvents()
            }
    }
}