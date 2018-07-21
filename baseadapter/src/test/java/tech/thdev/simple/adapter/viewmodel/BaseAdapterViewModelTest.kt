package tech.thdev.simple.adapter.viewmodel

import org.junit.Before
import org.junit.Test
import tech.thdev.simple.adapter.data.source.AdapterRepository
import tech.thdev.simple.adapter.util.cast

class BaseAdapterViewModelTest {

    private lateinit var baseAdapterViewModel: BaseAdapterViewModel

    @Before
    fun setUp() {
        baseAdapterViewModel = object : BaseAdapterViewModel(AdapterRepository()) {}
    }

    /**
     * Any type 저장 후 꺼내올 때 오류 체크
     */
    @Test
    fun testAddAndGetItem() {
        val fiveItem: Double = 5.0
        val list = mutableListOf("A", "B", 3, 4, fiveItem, 6.0, "88")
        list.forEach {
            baseAdapterViewModel.adapterRepository.addItem(0, it)
        }

        println("out = ${baseAdapterViewModel.adapterRepository.getItem(4).cast<Double>()}")
        assert(baseAdapterViewModel.adapterRepository.getItem(4).cast<Double>() != null)
    }

    @Test
    fun testRemoveAt() {
        val list = mutableListOf("A", 1, 1.0, "1")
        baseAdapterViewModel.adapterRepository.addItems(0, list)

        list.removeAt(0)
        baseAdapterViewModel.adapterRepository.removeAt(0)

        assert(list[0] == baseAdapterViewModel.adapterRepository.getItem(0))
    }
}