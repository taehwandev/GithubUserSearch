package tech.thdev.githubusersearch.view.common.adapter.holder

import android.view.ViewGroup
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.data.GithubUser
import tech.thdev.githubusersearch.view.common.adapter.viewmodel.UserAdapterViewModel
import tech.thdev.simple.adapter.holder.BaseViewHolder

class UserViewHolder(parent: ViewGroup) :
        BaseViewHolder<GithubUser, UserAdapterViewModel>(R.layout.item_user, parent) {

    override fun UserAdapterViewModel.onInitViewModel() {

    }

    override fun onBindViewHolder(item: GithubUser?) {

    }
}