package tech.thdev.githubusersearch.feature.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import tech.thdev.githubusersearch.feature.main.adapter.holder.UserViewHolder
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

class UserRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<UserViewHolder>() {

    private val list = mutableListOf<MainListUiState.UserItems.Info>()

    var onClick: (item: MainListUiState.UserItems.Info) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(parent, onClick = { position ->
            onClick(list[position])
        })

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int =
        list.size

    fun setItems(items: List<MainListUiState.UserItems.Info>) {
        val diff = DiffDefault(list, items)
        val diffResult = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    private class DiffDefault(
        private val oldItems: List<MainListUiState.UserItems.Info>,
        private val newItems: List<MainListUiState.UserItems.Info>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int =
            oldItems.size

        override fun getNewListSize(): Int =
            newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return oldItem == newItem
        }
    }
}