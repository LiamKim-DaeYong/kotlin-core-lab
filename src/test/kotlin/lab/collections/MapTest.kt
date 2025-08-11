package lab.collections

import kotlin.test.Test
import kotlin.test.assertEquals

class MapTest {
    @Test
    fun mapManual_basic() {
        val xs = listOf(1, 2, 3)
        val r = mapManual(xs) { it * 2 }
        assertEquals(listOf(2, 4, 6), r)
    }

    @Test
    fun mapManualInline_basic() {
        val xs = listOf("a", "bb")
        val r = mapManualInline(xs) { it.length }
        assertEquals(listOf(1, 2), r)
    }
}