package lab.scope

fun main() {
    val text = with(StringBuilder()) {
        appendLine("==Report==")
        appendLine("Item A: 10")
        appendLine("Item B: 20")
        toString()
    }
    println(text)

    val r1 = StringBuilder().run {
        append("Hello ")
        append("Run")
        toString()
    }

    val r2 = with(StringBuilder()) {
        append("Hello ")
        append("With")
        toString()
    }

    println("r1: $r1")
    println("r2: $r2")

    val list = mutableListOf("a", "b", "c")
    val size = with(list) {
        add("d")
        add("e")
        println("current: $this")
        size
    }
    println("size: $size")
}