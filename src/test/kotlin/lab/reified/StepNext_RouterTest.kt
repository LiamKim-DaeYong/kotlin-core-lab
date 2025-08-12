package lab.reified

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class StepNext_RouterTest {

    @Test
    fun castOrNull_basic() {
        val a: Int? = castOrNull<Int>(123)
        val b: String? = castOrNull<String>(123)
        assertEquals(123, a)
        assertNull(b)
    }

    @Test
    fun routeIf_basic() {
        val out = StringBuilder()
        routeIf<Int>(10, { out.append("int:$it;") }) { out.append("not-int;") }
        routeIf<String>(10, { out.append("str:$it;") }) { out.append("not-str;") }
        assertEquals("int:10;not-str;", out.toString())
    }

    @Test
    fun list_limitation() {
        val xs: Any = listOf(1, 2, 3)
        val ys: Any = listOf("a", "b")
        val lx: List<Int>? = castOrNull<List<Int>>(xs)
        val ly: List<Int>? = castOrNull<List<Int>>(ys)
        assertNotNull(lx)
        assertNotNull(ly)
    }

    @Test
    fun demoRoute_all() {
        assertEquals("int:42;not-str;list;", demoRoute())
    }
}