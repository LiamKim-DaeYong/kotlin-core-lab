package lab.reified

inline fun <reified T> List<*>.filterIsType(): List<T> {
    return this.filter { it is T }.map { it as T }
}

fun main() {
    val mixedList: List<Any> = listOf(1, "Hello", 3.14, "Kotlin", 42)

    val strings: List<String> = mixedList.filterIsType()
    val ints: List<Int> = mixedList.filterIsType<Int>()

    println("Strings: $strings")
    println("Ints: $ints")
}