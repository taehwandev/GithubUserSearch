package tech.thdev.githubusersearch.feature.main.like

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.databinding.FragmentLikeBinding
import tech.thdev.githubusersearch.feature.main.LikeChangeViewModel
import tech.thdev.githubusersearch.feature.main.adapter.UserRecyclerAdapter
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

@AndroidEntryPoint
class LikeFragment : Fragment() {

    @Inject
    lateinit var userRecyclerAdapter: UserRecyclerAdapter

    @Inject
    lateinit var likeViewModelFactory: LikeViewModel.LikeAssistedFactory

    private val likeViewModel by viewModels<LikeViewModel> {
        LikeViewModel.provideFactory(likeViewModelFactory, false)
    }

    private val likeChangeViewModel by viewModels<LikeChangeViewModel>()

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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                likeViewModel.mainListUiState
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