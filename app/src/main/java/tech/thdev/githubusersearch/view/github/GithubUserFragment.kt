package tech.thdev.githubusersearch.view.github

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.fragment_user_search.*
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.data.source.search.GithubSearchRepository
import tech.thdev.githubusersearch.util.adapterScrollLinearLayoutManagerListener
import tech.thdev.githubusersearch.util.autoRelease
import tech.thdev.githubusersearch.util.inject
import tech.thdev.githubusersearch.view.common.adapter.UserRecyclerAdapter
import tech.thdev.githubusersearch.view.common.viewmodel.FilterStatusViewModel
import tech.thdev.githubusersearch.view.common.viewmodel.SearchQueryViewModel
import tech.thdev.githubusersearch.view.github.viewmodel.GithubUserFragmentViewModel

class GithubUserFragment : Fragment() {

    private lateinit var githubSearchRepository: GithubSearchRepository

    companion object {
        private const val KEY_VIEW_TYPE = "key-view-type"
        const val VIEW_TYPE_SEARCH = 100
        const val VIEW_TYPE_LIKED = 200

        fun getInstance(viewType: Int, githubSearchRepository: GithubSearchRepository) =
                GithubUserFragment().apply {
                    this.githubSearchRepository = githubSearchRepository
                    arguments = Bundle().apply {
                        putInt(KEY_VIEW_TYPE, viewType)
                    }
                }
    }

    private val viewType: Int
        get() = arguments?.getInt(KEY_VIEW_TYPE) ?: VIEW_TYPE_SEARCH

    private val searchQueryViewModel: SearchQueryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        SearchQueryViewModel::class.java.inject(requireActivity()) {
            SearchQueryViewModel()
        }
    }

    private val filterStatusViewModel: FilterStatusViewModel by lazy(LazyThreadSafetyMode.NONE) {
        FilterStatusViewModel::class.java.inject(requireActivity()) {
            FilterStatusViewModel()
        }
    }

    private val userRecyclerAdapter: UserRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        UserRecyclerAdapter()
    }

    private var githubUserFragmentViewModel: GithubUserFragmentViewModel by autoRelease()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_user_search, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        githubUserFragmentViewModel = GithubUserFragmentViewModel::class.java.inject(this) {
            GithubUserFragmentViewModel(
                    viewType = viewType,
                    adapterViewModel = userRecyclerAdapter.viewModel,
                    githubSearchRepository = githubSearchRepository)
        }.also {
            it.viewInit()
            it.loadGithubUser(searchQueryViewModel.prevSearchQuery,
                    filterStatusViewModel.prevFilterType)
        }

        viewInit()

        searchQueryViewModel.viewInit()
        filterStatusViewModel.viewInit()
    }

    private fun viewInit() {
        recycler_view.run {
            layoutManager = LinearLayoutManager(this@GithubUserFragment.requireContext())
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

    private fun GithubUserFragmentViewModel.viewInit() {
        onShowProgress = {
            group_loading.visibility = View.VISIBLE
        }

        onHideProgress = {
            group_loading.visibility = View.GONE
        }

        onShowNetworkError = {
            getString(R.string.message_network_error).showErrorView()
        }

        onShowOtherError = {
            (it ?: getString(R.string.message_unknown_error)).showErrorView()
        }
    }

    private fun String.showErrorView() {
        this@GithubUserFragment.context?.toast(this)

        if (userRecyclerAdapter.itemCount == 0) {
            recycler_view.visibility = View.GONE
            group_error.visibility = View.VISIBLE
            tv_error_message.text = this

            btn_error.setOnClickListener {
                recycler_view.visibility = View.VISIBLE
                group_error.visibility = View.GONE
                githubUserFragmentViewModel.run {
                    initSearchQuerySubject()
                    search(searchQueryViewModel.prevSearchQuery)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        recycler_view?.removeOnScrollListener(adapterScrollListener)
    }
}
