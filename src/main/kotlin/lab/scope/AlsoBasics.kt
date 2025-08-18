package lab.scope

fun main() {
    val name = "  Alice "
        .trim()
        .also { println("[DEBUG] after trim: $it") }
        .uppercase()
        .also { require(it.length in 1..10) { "too long" } }
    println("name: $name")

    data class Client(var timeout: Int = 0, val tags: MutableList<String> = mutableListOf())
    val client = Client()
        .apply { timeout = 3_000; tags += "init" }
        .also { println("[Log] configured: $it") }
        .apply { tags += "ready" }
    println("client: $client")

    val ports = listOf("8080", "x", "  9090")
        .also { println("[RAW] $it") }
        .map { it.trim() }
        .also { println("[TRIM] $it") }
        .mapNotNull { it.toIntOrNull() }
        .also { println("[INT] $it") }
        .filter { it in 1..65535 }
        .also { println("[RANGE] $it") }
    println("ports: $ports")

    val email = "USER@EXAMPLE.COM"
        .also { println("[before] $it") }
        .let { it.lowercase() }
        .also { println("[after] $it") }
    println("email: $email")
}