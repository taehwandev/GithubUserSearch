package tech.thdev.githubusersearch.view.common.adapter

import android.content.res.Resources
import android.view.ViewGroup
import tech.thdev.githubusersearch.view.common.adapter.holder.UserSectionViewHolder
import tech.thdev.githubusersearch.view.common.adapter.holder.UserViewHolder
import tech.thdev.githubusersearch.view.common.adapter.viewmodel.UserAdapterViewModel
import tech.thdev.githubusersearch.view.common.adapter.viewmodel.UserAdapterViewModel.Companion.VIEW_TYPE_ITEM
import tech.thdev.githubusersearch.view.common.adapter.viewmodel.UserAdapterViewModel.Companion.VIEW_TYPE_SECTION
import tech.thdev.simple.adapter.BaseRecyclerViewAdapter
import tech.thdev.simple.adapter.holder.BaseViewHolder

class UserRecyclerAdapter : BaseRecyclerViewAdapter<UserAdapterViewModel>(::UserAdapterViewModel) {

    override fun createViewHolder(viewType: Int, parent: ViewGroup): BaseViewHolder<*, UserAdapterViewModel> =
            when (viewType) {
                VIEW_TYPE_SECTION -> UserSectionViewHolder(parent)
                VIEW_TYPE_ITEM -> UserViewHolder(parent)
                else -> throw Resources.NotFoundException("Not set view type")
            }
}