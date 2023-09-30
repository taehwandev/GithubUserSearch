package tech.thdev.simple.adapter.data

/**
 * Adapter item.
 */
sealed class BaseAdapterItem {

    data class DefaultAdapterItem(
        val viewType: Int,
        val item: Any?,
    ) : BaseAdapterItem()
}