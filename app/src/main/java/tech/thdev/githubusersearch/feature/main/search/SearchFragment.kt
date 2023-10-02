package tech.thdev.githubusersearch.feature.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.data.GitHubSearchRepositoryImpl
import tech.thdev.githubusersearch.database.GitHubDatabase
import tech.thdev.githubusersearch.databinding.FragmentSearchBinding
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.feature.main.FilterStatusViewModel
import tech.thdev.githubusersearch.feature.main.LikeChangeViewModel
import tech.thdev.githubusersearch.feature.main.SearchQueryViewModel
import tech.thdev.githubusersearch.feature.main.adapter.UserRecyclerAdapter
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.network.RetrofitFactory
import tech.thdev.githubusersearch.util.adapterScrollLinearLayoutManagerListener

class SearchFragment : Fragment() {

    private val gitHubSearchRepository: GitHubSearchRepository by lazy {
        GitHubSearchRepositoryImpl.getInstance(
            gitHubApi = RetrofitFactory.gitHubApi,
            gitHubUserDao = GitHubDatabase.getInstance(requireActivity().application).gitHubUserDao(),
        )
    }

    private val userRecyclerAdapter: UserRecyclerAdapter by lazy {
        UserRecyclerAdapter()
    }

    private val searchQueryViewModel by activityViewModels<SearchQueryViewModel>()

    private val filterStatusViewModel by activityViewModels<FilterStatusViewModel>()

    @Suppress("UNCHECKED_CAST")
    private val searchViewModel by viewModels<SearchViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        gitHubSearchRepository = gitHubSearchRepository,
                    ) as T
                }
            }
        }
    )

    @Suppress("UNCHECKED_CAST")
    private val likeChangeViewModel by viewModels<LikeChangeViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LikeChangeViewModel(
                        gitHubSearchRepository = gitHubSearchRepository,
                    ) as T
                }
            }
        }
    )

    private val adapterScrollListener by lazy {
        adapterScrollLinearLayoutManagerListener(searchViewModel::loadMore)
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSearchBinding.inflate(inflater).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewInit()
    }

    private fun viewInit() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.mainListUiState
                    .onEach {
                        when (it) {
                            is MainListUiState.Error -> {
                                (it.message ?: getString(R.string.message_unknown_error)).showErrorView()
                            }

                            is MainListUiState.UserItems -> {
                                showEmptyView(it.items.isEmpty())
                                userRecyclerAdapter.setItems(it.items)
                            }
                        }
                    }
                    .launchIn(this)

                searchViewModel.showProgress
                    .onEach { showProgress ->
                        if (showProgress) {
                            binding.groupLoading.visibility = View.VISIBLE
                        } else {
                            binding.groupLoading.visibility = View.GONE
                        }
                    }
                    .launchIn(this)

                searchQueryViewModel.searchQuery
                    .onEach(searchViewModel::updateKeyword)
                    .launchIn(this)

                filterStatusViewModel.filterUiState
                    .onEach {
                        searchViewModel.changeSort(it.sortType)
                    }
                    .launchIn(this)
            }
        }

        binding.recyclerView.run {
            addOnScrollListener(adapterScrollListener)
            adapter = userRecyclerAdapter
        }

        userRecyclerAdapter.onClick = {
            likeChangeViewModel.selectedLikeChange(it)
        }
    }

    private fun showEmptyView(isEmpty: Boolean) {
        binding.tvUserMessage.visibility = if (isEmpty) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun String.showErrorView() {
        binding.recyclerView.visibility = View.GONE
        binding.tvUserMessage.run {
            visibility = View.VISIBLE
            text = this@showErrorView
        }
        binding.btnUserBehavior.visibility = View.VISIBLE

        binding.btnUserBehavior.setOnClickListener {
            binding.recyclerView.visibility = View.VISIBLE
            binding.tvUserMessage.visibility = View.GONE
            binding.btnUserBehavior.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.recyclerView.removeOnScrollListener(adapterScrollListener)
        _binding = null
    }
}