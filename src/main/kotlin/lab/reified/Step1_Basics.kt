package lab.reified

inline fun <reified T> isTypeReified(value: Any): Boolean = value is T

fun checkInt(x: Any) = isTypeReified<Int>(x)
fun checkString(x: Any) = isTypeReified<String>(x)
fun checkListInt(x: Any)= isTypeReified<List<Int>>(x)

fun demo1(): List<Boolean> = listOf(
    isTypeReified<Int>(42),
    isTypeReified<String>(42),
    isTypeReified<List<Int>>(listOf(1))
)

data class Foo(val x: Int = 0)

inline fun <reified T> printTypeInfo() {
    println("KClass: ${T::class}")
    println("Java Class: ${T::class.java}")
}

fun main() {
    printTypeInfo<Foo>()
    printTypeInfo<String>()
}