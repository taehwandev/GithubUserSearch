package tech.thdev.githubusersearch.view.search

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
import tech.thdev.githubusersearch.network.RetrofitFactory
import tech.thdev.githubusersearch.util.inject
import tech.thdev.githubusersearch.view.common.adapter.UserRecyclerAdapter
import tech.thdev.githubusersearch.view.common.viewmodel.SearchQueryViewModel
import tech.thdev.githubusersearch.view.search.viewmodel.SearchFragmentViewModel

class SearchFragment : Fragment() {

    companion object {
        fun getInstance(title: String) =
                SearchFragment().apply {
                    arguments = Bundle().apply {
                        putString("test", title)
                    }
                }
    }

    private val searchQueryViewModel: SearchQueryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        SearchQueryViewModel::class.java.inject(requireActivity()) {
            SearchQueryViewModel()
        }
    }

    private val searchFragmentViewModel: SearchFragmentViewModel by lazy(LazyThreadSafetyMode.NONE) {
        SearchFragmentViewModel::class.java.inject(this) {
            SearchFragmentViewModel(
                    adapterViewModel = userRecyclerAdapter.viewModel,
                    githubSearchRepository = GithubSearchRepository.getInstance(RetrofitFactory.githubApi))
        }
    }

    private val userRecyclerAdapter: UserRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        UserRecyclerAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_user_search, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewInit()

        searchQueryViewModel.init()
        searchFragmentViewModel.init()
    }

    private fun viewInit() {
        recycler_view.run {
            layoutManager = LinearLayoutManager(this@SearchFragment.requireContext())
            adapter = userRecyclerAdapter
        }
    }

    private fun SearchQueryViewModel.init() {
        updateSearchQuery = searchFragmentViewModel::search
    }

    private fun SearchFragmentViewModel.init() {
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
        this@SearchFragment.context?.toast(this)

        recycler_view.visibility = View.GONE
        group_error.visibility = View.VISIBLE
        tv_error_message.text = this

        btn_error.setOnClickListener {
            recycler_view.visibility = View.VISIBLE
            group_error.visibility = View.GONE
            searchFragmentViewModel.run {
                initSearchQuerySubject()
                search()
            }
        }
    }
}