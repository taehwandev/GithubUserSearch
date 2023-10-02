package tech.thdev.githubusersearch.feature.main.like

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.data.GitHubSearchRepositoryImpl
import tech.thdev.githubusersearch.database.GitHubDatabase
import tech.thdev.githubusersearch.databinding.FragmentLikeBinding
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.feature.main.LikeChangeViewModel
import tech.thdev.githubusersearch.feature.main.adapter.UserRecyclerAdapter
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.network.RetrofitFactory

class LikeFragment : Fragment() {

    private val gitHubSearchRepository: GitHubSearchRepository by lazy {
        GitHubSearchRepositoryImpl.getInstance(
            gitHubApi = RetrofitFactory.gitHubApi,
            gitHubUserDao = GitHubDatabase.getInstance(requireActivity().application).gitHubUserDao(),
        )
    }

    private val userRecyclerAdapter: UserRecyclerAdapter by lazy {
        UserRecyclerAdapter()
    }

    @Suppress("UNCHECKED_CAST")
    private val likeViewModel by viewModels<LikeViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LikeViewModel(
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

    private var _binding: FragmentLikeBinding? = null
    private val binding: FragmentLikeBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentLikeBinding.inflate(inflater).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewInit()
    }

    private fun viewInit() {
        likeViewModel.mainListUiState.observe(viewLifecycleOwner) {
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

        binding.recyclerView.adapter = userRecyclerAdapter

        userRecyclerAdapter.onClick = {
            likeChangeViewModel.selectedLikeChange(it)
        }
    }

    private fun showEmptyView(isEmpty: Boolean) {
        if (isEmpty) {
            binding.tvUserMessage.run {
                visibility = View.VISIBLE
                setText(R.string.like_hint)
            }
        } else {
            binding.tvUserMessage.visibility = View.GONE
        }
    }

    private fun String.showErrorView() {
        binding.recyclerView.visibility = View.GONE
        binding.tvUserMessage.run {
            visibility = View.VISIBLE
            text = this@showErrorView
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}