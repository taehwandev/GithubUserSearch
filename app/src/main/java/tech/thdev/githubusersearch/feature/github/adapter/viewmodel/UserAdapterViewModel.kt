package tech.thdev.githubusersearch.feature.github.adapter.viewmodel

import android.util.Log
import tech.thdev.simple.adapter.data.source.AdapterRepositoryInterface
import tech.thdev.simple.adapter.util.cast
import tech.thdev.simple.adapter.viewmodel.BaseAdapterViewModel

class UserAdapterViewModel(
    adapterRepository: AdapterRepositoryInterface,
) : BaseAdapterViewModel(adapterRepository) {

    init {
        Log.d("TEMP", "adapterViewModel ${this.hashCode()}")
    }

    companion object {
        const val VIEW_TYPE_SECTION = 1000
        const val VIEW_TYPE_ITEM = 2000
    }

    lateinit var onLikeUserInfo: (adapterPosition: Int, item: GitHubUser) -> Unit

    lateinit var onUnlikeUserInfo: (adapterPosition: Int, item: GitHubUser) -> Unit

    fun onClickUserItem(adapterPosition: Int) {
        adapterRepository.getItem(adapterPosition).cast<GitHubUser>()?.let {
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