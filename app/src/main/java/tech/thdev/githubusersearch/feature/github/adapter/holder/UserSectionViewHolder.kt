package tech.thdev.githubusersearch.feature.github.adapter.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import tech.thdev.githubusersearch.databinding.ItemUserSectionBinding
import tech.thdev.githubusersearch.feature.github.adapter.viewmodel.UserAdapterViewModel
import tech.thdev.simple.adapter.holder.BaseViewHolder

class UserSectionViewHolder(
    parent: ViewGroup,
    private val binding: ItemUserSectionBinding = ItemUserSectionBinding.inflate(LayoutInflater.from(parent.context)),
) :
    BaseViewHolder<String, UserAdapterViewModel>(binding.root) {

    override fun UserAdapterViewModel.onInitViewModel() {
        // Do nothing.
    }

    override fun onBindViewHolder(item: String?) {
        binding.tvTitle.text = item
    }
}