package tech.thdev.githubusersearch.view.github.adapter.holder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_user.*
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.data.GithubUser
import tech.thdev.githubusersearch.util.loadUrl
import tech.thdev.githubusersearch.view.github.adapter.viewmodel.UserAdapterViewModel
import tech.thdev.simple.adapter.holder.BaseViewHolder

class UserViewHolder(parent: ViewGroup) :
        BaseViewHolder<GithubUser, UserAdapterViewModel>(R.layout.item_user, parent) {

    init {
        containerView.setOnClickListener {
            viewModel.onClickUserItem(adapterPosition)
        }
    }

    override fun UserAdapterViewModel.onInitViewModel() {

    }

    override fun onBindViewHolder(item: GithubUser?) {
        img_user_picture.loadUrl(item?.avatarUrl, R.drawable.ic_launcher_foreground)
        tv_user_name.text = item?.login
        tv_user_score.text = context.getString(R.string.message_user_score, item?.score ?: 0.0)
        img_user_like.isSelected = item?.isLike ?: false
    }
}