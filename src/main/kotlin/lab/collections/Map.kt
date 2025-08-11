package lab.collections

fun <T, R> mapManual(
    source: Iterable<T>,
    transform: (T) -> R,
): List<R> {
    val result = ArrayList<R>()
    for (e in source) result.add(transform(e))
    return result
}

inline fun <T, R> mapManualInline(
    source: Iterable<T>,
    transform: (T) -> R,
): List<R> {
    val result = ArrayList<R>()
    for (e in source) result.add(transform(e))
    return result
}