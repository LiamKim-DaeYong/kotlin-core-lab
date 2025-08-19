package lab.generics

open class Fruit { override fun toString() = this::class.simpleName ?: "Fruit" }
class Apple : Fruit()
class Banana : Fruit()

class Box<out T>(private val value: T) {
    fun get(): T = value
}

interface Sink<in T> { fun put(item: T) }
class PrintSink : Sink<Any> {
    override fun put(item: Any) { println("Sink got: $item") }
}

fun feedApple(sink: Sink<Apple>) { sink.put(Apple()) }

fun <T> copyAll(from: List<out T>, to: MutableList<in T>) {
    to.addAll(from)
}

val anyPrinter: (Any) -> Unit = { println("Any: $it") }
val stringPrinter: (String) -> Unit = anyPrinter

val stringProducer: () -> String = { "hello" }
val anyProducerFun: () -> Any = stringProducer

interface Producer<out T> { fun produce(): T }
interface Consumer<in T> { fun consume(t: T) }

class StringProducer : Producer<String> { override fun produce() = "Hi" }
class AnyConsumer : Consumer<Any> { override fun consume(t: Any) = println("Consumed $t") }

fun main() {
    val apples: List<Apple> = listOf(Apple(), Apple())
    val fruits: List<Fruit> = apples
    val f: Fruit = fruits[0]
    println("First fruit: $f")

    val sinkAny: Sink<Any> = PrintSink()
    val sinkApple: Sink<Apple> = sinkAny
    feedApple(sinkApple)

    val dest: MutableList<Fruit> = mutableListOf(Banana())
    copyAll(apples, dest)
    println("After copyAll, dest size = ${dest.size} → ${dest.joinToString()}")

    stringPrinter("Kotlin")
    println("Produced: ${anyProducerFun()}")

    val p: Producer<Any> = StringProducer()
    val c: Consumer<String> = AnyConsumer()
    println("Producer gave: ${p.produce()}")
    c.consume("Variance!")

    val unknowns: List<*> = listOf(1, "x", 3.14)
    println("Star-projection read: ${unknowns[0]}")
}
