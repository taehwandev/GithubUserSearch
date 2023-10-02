package tech.thdev.githubusersearch.feature.main

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SearchQueryViewModelTest {

    private val viewModel = SearchQueryViewModel()

    @Test
    fun `test initData`() {
        Assertions.assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `test setSearchQuery`() {
        viewModel.setSearchQuery("query")
        Assertions.assertEquals("query", viewModel.searchQuery.value)
    }
}