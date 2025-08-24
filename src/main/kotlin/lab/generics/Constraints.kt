package lab.generics

class Constraints {
    class Box<T: Comparable<T>>(private val value: T) {
        fun maxWith(other: T): T = if (value >= other) value else other
        override fun toString(): String = "Box($value)"
    }

    interface Jsonable { fun toJson(): String }

    open class IdEntity(open val id: Long)

    class Person(override val id: Long, val name: String): IdEntity(id), Jsonable {
        override fun toJson(): String = """{"id":$id, "name": "$name"}"""
        override fun toString(): String = "Person(id=$id, name=$name)"
    }
}

fun <T : Number> printDouble(x: T) {
    println("double = ${x.toDouble() * 2}")
}

fun <T> maxOf(a: T, b: T): T
    where T: Number, T: Comparable<T> {
        return if (a > b) a else b
    }

fun <T> describeEntity(t: T)
    where T : Constraints.IdEntity, T: Constraints.Jsonable {
        println("Entity description: ${t.toJson()}")
    }

fun <T> sumOf(a: T, b: T): Double
    where T: Number {
        return a.toDouble() + b.toDouble()
    }

fun <T: Any> notNullEcho(x: T): T = x
fun <T: Any?> nullableEcho(x: T): T = x

fun <T: CharSequence> isBlankOrNull(cs: T?): Boolean =
    cs == null || cs.isBlank()

fun main() {
    println("== Example 1: Single upper bound ==")
    printDouble(21)
    printDouble(3.14)

    println("\n== Example 2: Multiple constraints via 'where' ==")
    println("maxOf(3, 7) = ${maxOf(3, 7)}")
    println("maxOf(2.2, 1.5) = ${maxOf(2.2, 1.5)}")

    println("\n== Example 3: Generic class with upper bound ==")
    val b1 = Constraints.Box(10)
    val b2 = Constraints.Box("kotlin")
    println("b1.maxWith(7) = ${b1.maxWith(7)}")
    println("b2.maxWith(\"java\") = ${b2.maxWith("java")}")

    println("\n== Example 4: Class + interfaces bounds ==")
    val p = Constraints.Person(1L, "Alice")
    describeEntity(p)

    println("\n== Example 5: Numeric sum with Number bound ==")
    println("sumOf(10, 2) = ${sumOf(10, 2)}")
    println("sumOf(1.5, 2.25) = ${sumOf(1.5, 2.25)}")

    println("\n== Example 6: Nullable bounds ==")
    println(notNullEcho("hello"))
    println(nullableEcho<String?>(null))

    println("\n== Example 7: CharSequence bound ==")
    println(isBlankOrNull("   "))
    println(isBlankOrNull(null))
    println(isBlankOrNull("text"))
}