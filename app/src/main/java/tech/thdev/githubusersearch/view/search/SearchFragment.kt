package tech.thdev.githubusersearch.view.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_user_section.*
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.util.inject
import tech.thdev.githubusersearch.view.common.viewmodel.SearchQueryViewModel

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
        }.apply {
            updateSearchQuery = {
                Log.d("TEMP", "updateSearchQuery!!!!! $it")
                tv_title?.text = it
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_user_search, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchQueryViewModel
        tv_title.text = arguments?.getString("test")
    }
}