package lab.generics

inline fun <T, R> Iterable<T>.myMapIndexed(
    transform: (index: Int, element: T) -> R,
): List<R> {
    val result = when (this) {
        is Collection<*> -> ArrayList<R>(this.size)
        else -> ArrayList()
    }
    var i = 0
    for (e in this) {
        result.add(transform(i, e))
        i += 1
    }

    return result
}

fun <T, R> Sequence<T>.myMapIndexed(
    transform: (index: Int, element: T) -> R,
): Sequence<R> = sequence {
    var i = 0
    for (e in this@myMapIndexed) {
        yield(transform(i, e))
        i += 1
    }
}