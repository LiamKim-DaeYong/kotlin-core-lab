package lab.generics

class StarProjection {
    open class Fruit { override fun toString() = this::class.simpleName ?: "Fruit" }
    class Apple : Fruit()
    class Banana : Fruit()
}

fun printAll(list: List<*>) {
    for (e in list) {
        println("elem: $e")
    }
}

fun head(list: List<*>): Any? {
    return if (list.isNotEmpty()) list[0] else null
}

fun drainOne(producer: MutableList<out StarProjection.Fruit>): StarProjection.Fruit? {
    return if (producer.isNotEmpty()) producer.removeAt(0) else null
}

fun fillWithApples(consumer: MutableList<in StarProjection.Apple>) {
    consumer.add(StarProjection.Apple())
}

fun printMap(m: Map<*, *>) {
    for ((k, v) in m) {
        println("k=$k, v=$v")
    }
}

@Suppress("UNCHECKED_CAST")
fun unsafeSum(list: List<*>): Int {
    val ints = list as List<Int>
    return ints.sum()
}

fun main() {
    val strings: List<String> = listOf("A", "B", "C")
    val numbers: List<Int> = listOf(1, 2, 3)
    val mixed: List<Any?> = listOf("X", 42, true, null)

    val s1: List<*> = strings
    val s2: List<*> = numbers
    val s3: List<*> = mixed

    println("== printAll(List<*>) ==")
    printAll(s1)
    printAll(s2)
    printAll(s3)

    println("\n== head(List<*>) ==")
    println("head(strings): ${head(s1)}")
    println("head(numbers): ${head(s2)}")
    println("head(mixed):   ${head(s3)}")

    println("\n== use-site variance vs star projection ==")
    val apples = mutableListOf(StarProjection.Apple(), StarProjection.Apple())
    val fruits = mutableListOf<StarProjection.Fruit>(StarProjection.Banana())

    val producer: MutableList<out StarProjection.Fruit> = apples
    println("drainOne from producer: ${drainOne(producer)}")

    val consumer: MutableList<in StarProjection.Apple> = fruits
    fillWithApples(consumer)
    println("after fillWithApples: $fruits")

    println("\n== Map<*,*> ==")
    val m: Map<*, *> = mapOf("a" to 1, "b" to 2)
    printMap(m)

    println("\n== UNSAFE cast demo (may crash if list is not List<Int>) ==")
    println("unsafeSum(numbers) = ${unsafeSum(numbers)}")
}