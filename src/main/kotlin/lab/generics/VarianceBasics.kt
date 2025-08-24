package lab.generics

import lab.generics.VarianceBasics.AnyConsumer
import lab.generics.VarianceBasics.Apple
import lab.generics.VarianceBasics.Banana
import lab.generics.VarianceBasics.Consumer
import lab.generics.VarianceBasics.PrintSink
import lab.generics.VarianceBasics.Producer
import lab.generics.VarianceBasics.Sink
import lab.generics.VarianceBasics.StringProducer

class VarianceBasics {
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

    interface Producer<out T> { fun produce(): T }
    interface Consumer<in T> { fun consume(t: T) }

    class StringProducer : Producer<String> { override fun produce() = "Hi" }
    class AnyConsumer : Consumer<Any> { override fun consume(t: Any) = println("Consumed $t") }
}

fun feedApple(sink: Sink<Apple>) { sink.put(Apple()) }

fun <T> copyAll(from: List<out T>, to: MutableList<in T>) {
    to.addAll(from)
}

val anyPrinter: (Any) -> Unit = { println("Any: $it") }
val stringPrinter: (String) -> Unit = anyPrinter

val stringProducer: () -> String = { "hello" }
val anyProducerFun: () -> Any = stringProducer

fun main() {
    val apples: List<VarianceBasics.Apple> = listOf(Apple(), Apple())
    val fruits: List<VarianceBasics.Fruit> = apples
    val f: VarianceBasics.Fruit = fruits[0]
    println("First fruit: $f")

    val sinkAny: Sink<Any> = PrintSink()
    val sinkApple: Sink<Apple> = sinkAny
    feedApple(sinkApple)

    val dest: MutableList<VarianceBasics.Fruit> = mutableListOf(Banana())
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