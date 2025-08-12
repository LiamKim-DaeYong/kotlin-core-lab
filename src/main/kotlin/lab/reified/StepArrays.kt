package lab.reified

import java.lang.reflect.Array as JArray

@Suppress("UNCHECKED_CAST")
inline fun <reified T> newArray(size: Int): Array<T?> =
    JArray.newInstance(T::class.java, size) as Array<T?>

fun demoNewArray(): Pair<Array<Int?>, Array<String?>> {
    val ai = newArray<Int>(3)
    val as_ = newArray<String>(2)
    return ai to as_
}