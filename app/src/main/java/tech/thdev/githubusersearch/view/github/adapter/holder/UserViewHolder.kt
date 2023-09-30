package tech.thdev.githubusersearch.view.github.adapter.holder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.data.GitHubUser
import tech.thdev.githubusersearch.databinding.ItemUserBinding
import tech.thdev.githubusersearch.util.loadUrl
import tech.thdev.githubusersearch.view.github.adapter.viewmodel.UserAdapterViewModel
import tech.thdev.simple.adapter.holder.BaseViewHolder

class UserViewHolder(
    parent: ViewGroup,
    private val binding: ItemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context)),
) : BaseViewHolder<GitHubUser, UserAdapterViewModel>(binding.root) {

    override fun UserAdapterViewModel.onInitViewModel() {
        binding.root.setOnClickListener {
            onClickUserItem(adapterPosition = absoluteAdapterPosition)
        }
    }

    override fun onBindViewHolder(item: GitHubUser?) {
        Log.i("TEMP", "item? $item")
        binding.imgUserPicture.loadUrl(item?.avatarUrl, R.drawable.ic_launcher_foreground)
        binding.tvUserName.text = item?.login
        binding.tvUserScore.text = binding.root.context.getString(R.string.message_user_score, item?.score ?: 0.0)
        binding.imgUserLike.isSelected = item?.isLike ?: false
    }
}