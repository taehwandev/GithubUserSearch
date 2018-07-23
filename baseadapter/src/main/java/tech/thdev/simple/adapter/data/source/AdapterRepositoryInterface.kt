package tech.thdev.simple.adapter.data.source

interface AdapterRepositoryInterface {

    val itemCount: Int

    fun getItem(position: Int): Any?

    fun removeAt(position: Int)

    fun getItemViewType(position: Int): Int

    fun addItem(viewType: Int, item: Any?)

    fun addItems(viewType: Int, item: List<Any?>?)

    fun clear()
}