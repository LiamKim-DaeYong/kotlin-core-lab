package lab.reified

import kotlin.test.Test
import kotlin.test.assertEquals

class TinyContainerTest {

    @Test
    fun `register and get by interface works`() {
        val c = TinyContainer()
        c.register(UserRepo::class) { InMemoryUserRepo() }

        val repo: UserRepo = c.get(UserRepo::class)

        assertEquals(listOf("amy", "bob"), repo.findAll())
    }

    @Test
    fun `get by interface fails if only implementation was registered`() {
        val c = TinyContainer()
        c.register<InMemoryUserRepo> { InMemoryUserRepo() }

        try {
            c.get<UserRepo>()
            error("Expected failure but succeeded")
        } catch (e: IllegalStateException) {
            println("Caught expected error: ${e.message}")
        }
    }
}