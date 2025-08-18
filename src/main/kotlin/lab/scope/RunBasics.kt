package lab.scope

fun main() {
    val summary = StringBuilder().run {
        append("Hello, ")
        append("Kotlin!")
        toString()
    }
    println("summary: $summary")

    val length = "Hello, Kotlin!".run {
        println("original: $this")
        length
    }
    println("length: $length")

    val maybe: String? = "scope-fun"
    val result = maybe?.run {
        "Length of '$this' is $length"
    } ?: "was null"
    println("result: $result")

    val input = "  hello "
    val r1 = input.let { it.trim().uppercase() }
    val r2 = input.run { trim().uppercase() }
    println("r1: $r1, r2: $r2")
}