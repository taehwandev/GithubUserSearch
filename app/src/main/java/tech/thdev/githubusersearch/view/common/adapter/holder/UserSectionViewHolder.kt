package tech.thdev.githubusersearch.view.common.adapter.holder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_user_section.*
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.view.common.adapter.viewmodel.UserAdapterViewModel
import tech.thdev.simple.adapter.holder.BaseViewHolder

class UserSectionViewHolder(parent: ViewGroup) :
        BaseViewHolder<String, UserAdapterViewModel>(R.layout.item_user_section, parent) {

    override fun UserAdapterViewModel.onInitViewModel() {
        // Do nothing.
    }

    override fun onBindViewHolder(item: String?) {
        tv_title.text = item
    }
}