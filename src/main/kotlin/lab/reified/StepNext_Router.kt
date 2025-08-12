package lab.reified

inline fun <reified T> castOrNull(x: Any?): T? = x as? T

inline fun <reified T> routeIf(
    x: Any?,
    block: (T) -> Unit,
    elseBlock: () -> Unit = {}
) {
    if (x is T) block(x) else elseBlock()
}

fun demoRoute(): String {
    val sb = StringBuilder()
    routeIf<Int>(42, { sb.append("int:$it;") }) { sb.append("not-int;") }
    routeIf<String>(42, { sb.append("str:$it;") }) { sb.append("not-str;") }
    routeIf<List<Int>>(listOf(1,2), { sb.append("list;") }) { sb.append("not-list;") }
    return sb.toString()
}

fun main() {
    val list1 = listOf(1, 2, 3)
    val list2 = listOf("a", "b", "c")

    println(list1::class)
    println(list2::class)

    println(list1.javaClass)
    println(list2.javaClass)
}