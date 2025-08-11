package lab.inline

import kotlin.test.Test
import kotlin.test.assertEquals

class InlineBasicsTest {
    @Test
    fun basic_behaves_same() {
        assertEquals(20, callSiteInline())
        assertEquals(20, callSiteInline())
    }

    @Test
    fun generic_works() {
        val s1 = normalOp("ab") { it.length }
        val s2 = inlineOp("ab") { it.length }
        assertEquals(2, s1)
        assertEquals(2, s2)
    }

    @Test
    fun lambda_in_variable_behaves_same() {
        assertEquals(20, callSiteNormal_var())
        assertEquals(20, callSiteInline_var())
    }

    @Test
    fun inline_with_capture() {
        assertEquals(30, callSiteInline_capture())
    }
}