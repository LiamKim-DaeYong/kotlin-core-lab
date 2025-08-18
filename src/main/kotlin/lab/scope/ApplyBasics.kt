package lab.scope

data class HttpClient(
    var connectTimeout: Int = 0,
    var readTimeout: Int = 0,
    var headers: MutableMap<String, String> = mutableMapOf()
)

fun main() {
    val client = HttpClient().apply {
        connectTimeout = 3_000
        readTimeout = 5_000
        headers["X-Trace"] = "12345"
    }
    println("client: $client")

    val r1 = StringBuilder().apply {
        append("Hello ")
        append("Apply")
    }
    println("r1: $r1")

    val r2 = StringBuilder().run {
        append("Hello ")
        append("Run")
        toString()
    }

    data class User(val id: Int, val name: String)
    class UserBuilder {
        var id: Int = 0
        var name: String = ""
        fun build(): User = User(id, name)
    }

    val user = UserBuilder().apply {
        id = 1
        name = "Alice"
    }.build()
    println("user: $user")
}