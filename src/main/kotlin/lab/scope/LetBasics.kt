package lab.scope

fun main() {
    data class User(val id: Long, val email: String?, val phone: String?)

    val u1 = User(1, "Alice@Example.COM", "010-5522")
    val u2 = User(2, null, null)
    val u3 = User(3, "bob@example.com", "4442233")

    val domain1 = u1.email?.let { e ->
        e.lowercase().substringAfter("@")
    } ?: "unknown"
    val domain2 = u2.email?.let { it.lowercase().substringAfter("@") } ?: "unknown"

    println("domain1=$domain1")
    println("domain2=$domain2")

    val rawPorts = listOf("8080", "x", "443", " 9090 ")
    val ports = rawPorts
        .map { it.trim() }
        .mapNotNull { s -> s.toIntOrNull()?.let { if (it in 1..65535) it else null } }

    println("ports=$ports")

    val bufBad = StringBuilder()
    bufBad.let {
        it.append("hello, ")
        it.append(" ")
        it.append("kotlin")
    }
    println("bufBad=$bufBad")

    val bufGood = StringBuilder().run {
        append("hello"); append(" "); append("kotlin")
        toString()
    }
    println("bufGood=$bufGood")

    val phonePattern = Regex("\\d{3}-\\d{4}")

    u1.phone?.takeIf { it.matches(phonePattern) }?.let { println("valid: $it") } ?: println("invalid")
    u3.phone?.takeIf { it.matches(phonePattern) }?.let { println("valid: $it") } ?: println("invalid")
}