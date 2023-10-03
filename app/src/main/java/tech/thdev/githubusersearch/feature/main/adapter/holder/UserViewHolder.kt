package tech.thdev.githubusersearch.feature.main.adapter.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.base.adapter.holder.BaseViewHolder
import tech.thdev.githubusersearch.databinding.ItemUserBinding
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

class UserViewHolder(
    parent: ViewGroup,
    private val binding: ItemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context)),
    onClick: (position: Int) -> Unit,
) : BaseViewHolder<MainListUiState.UserItems.Info>(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClick(absoluteAdapterPosition)
        }
    }

    override fun onBind(item: MainListUiState.UserItems.Info) {
//        binding.imgUserPicture.loadUrl(item.avatarUrl, R.drawable.ic_launcher_foreground)
        binding.tvUserName.text = item.login
        binding.tvUserScore.text = binding.root.context.getString(R.string.message_user_score, item.score)
        binding.imgUserLike.isSelected = item.isLike
    }
}