package tech.thdev.githubusersearch.view.github.adapter.viewmodel

import tech.thdev.githubusersearch.data.GithubUser
import tech.thdev.simple.adapter.data.source.AdapterRepositoryInterface
import tech.thdev.simple.adapter.util.cast
import tech.thdev.simple.adapter.viewmodel.BaseAdapterViewModel

class UserAdapterViewModel(adapterRepository: AdapterRepositoryInterface) : BaseAdapterViewModel(adapterRepository) {

    companion object {
        const val VIEW_TYPE_SECTION = 1000
        const val VIEW_TYPE_ITEM = 2000
    }

    lateinit var onLikeUserInfo: (adapterPosition: Int, item: GithubUser) -> Unit

    lateinit var onUnlikeUserInfo: (adapterPosition: Int, item: GithubUser) -> Unit

    fun onClickUserItem(adapterPosition: Int) {
        adapterRepository.getItem(adapterPosition).cast<GithubUser>()?.let {
            if (it.isLike) {
                if (::onUnlikeUserInfo.isInitialized) {
                    onUnlikeUserInfo(adapterPosition, it)
                }
            } else {
                if (::onLikeUserInfo.isInitialized) {
                    onLikeUserInfo(adapterPosition, it)
                }
            }
        }
    }
}