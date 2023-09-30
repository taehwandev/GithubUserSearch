package tech.thdev.githubusersearch.feature.github

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.databinding.FragmentUserSearchBinding
import tech.thdev.githubusersearch.databinding.IncludeToastErrorBinding
import tech.thdev.githubusersearch.util.adapterScrollLinearLayoutManagerListener
import tech.thdev.githubusersearch.util.createErrorToast
import tech.thdev.githubusersearch.feature.github.adapter.UserRecyclerAdapter
import tech.thdev.githubusersearch.feature.github.viewmodel.FilterStatusViewModel
import tech.thdev.githubusersearch.feature.github.viewmodel.GitHubUserFragmentViewModel
import tech.thdev.githubusersearch.feature.github.viewmodel.SearchQueryViewModel

class GithubUserFragment : Fragment() {

    private lateinit var githubSearchRepository: GitHubSearchRepository

    private val viewType: Int
        get() = arguments?.getInt(KEY_VIEW_TYPE) ?: VIEW_TYPE_SEARCH

    private val searchQueryViewModel by activityViewModels<SearchQueryViewModel>()

    private val filterStatusViewModel by activityViewModels<FilterStatusViewModel>()

    private val userRecyclerAdapter: UserRecyclerAdapter by lazy {
        UserRecyclerAdapter()
    }

    private lateinit var githubUserFragmentViewModel: GitHubUserFragmentViewModel

    private var _binding: FragmentUserSearchBinding? = null
    private val binding: FragmentUserSearchBinding
        get() = _binding!!

    private fun showErrorToast(message: String) {
        requireContext().createErrorToast {
            IncludeToastErrorBinding.inflate(LayoutInflater.from(requireContext())).apply {
                tvErrorToast.text = message
            }.root
        }.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentUserSearchBinding.inflate(inflater).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDefaultView()

        githubUserFragmentViewModel = GitHubUserFragmentViewModel(
            viewType = viewType,
            adapterViewModel = userRecyclerAdapter.viewModel,
            githubSearchRepository = githubSearchRepository,
        ).also {
            it.viewInit()
            it.loadGithubUser(searchQueryViewModel.prevSearchQuery, filterStatusViewModel.prevFilterType)
        }
        viewInit()
        searchQueryViewModel.viewInit()
        filterStatusViewModel.viewInit()
    }

    private fun viewInit() {
        binding.recyclerView.run {
            if (viewType == VIEW_TYPE_SEARCH) {
                addOnScrollListener(adapterScrollListener)
            }
            adapter = userRecyclerAdapter
        }
    }

    private val adapterScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        adapterScrollLinearLayoutManagerListener(githubUserFragmentViewModel::loadMore)
    }

    private fun FilterStatusViewModel.viewInit() {
        updateFilterStatus = githubUserFragmentViewModel::changeFilter
    }

    private fun SearchQueryViewModel.viewInit() {
        updateSearchQuery = githubUserFragmentViewModel::search
        updateSearchQueryCommit = {
            githubUserFragmentViewModel.run {
                initSearchQuerySubject()
                search(it)
            }
        }
    }

    private fun GitHubUserFragmentViewModel.viewInit() {
        showEmptyView = {
            showDefaultView()
        }

        hideEmptyView = {
            binding.tvUserMessage.visibility = View.GONE
        }

        noSearchItem = {
            showErrorToast(getString(R.string.message_no_result_user_name))
            showDefaultView()
        }

        onShowProgress = {
            binding.groupLoading.visibility = View.VISIBLE
        }

        onHideProgress = {
            binding.groupLoading.visibility = View.GONE
        }

        onShowNetworkError = {
            getString(R.string.message_network_error).showErrorView()
        }

        onShowOtherError = {
            (it ?: getString(R.string.message_unknown_error)).showErrorView()
        }
    }

    private fun showDefaultView() {
        if (userRecyclerAdapter.itemCount == 0) {
            binding.tvUserMessage.run {
                visibility = View.VISIBLE
                setText(R.string.search_hint)
            }
        }
    }

    private fun String.showErrorView() {
        showErrorToast(this)

        if (userRecyclerAdapter.itemCount == 0) {
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
                githubUserFragmentViewModel.run {
                    initSearchQuerySubject()
                    search(searchQueryViewModel.prevSearchQuery)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.recyclerView.removeOnScrollListener(adapterScrollListener)
        _binding = null
    }

    companion object {
        private const val KEY_VIEW_TYPE = "key-view-type"
        const val VIEW_TYPE_SEARCH = 100
        const val VIEW_TYPE_LIKED = 200

        fun getInstance(viewType: Int, githubSearchRepository: GitHubSearchRepository) =
            GithubUserFragment().apply {
                this.githubSearchRepository = githubSearchRepository
                arguments = Bundle().apply {
                    putInt(KEY_VIEW_TYPE, viewType)
                }
            }
    }
}