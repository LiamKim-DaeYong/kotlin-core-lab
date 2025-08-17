package lab.reified

import kotlin.reflect.KClass

class TinyContainer {
    private val providers = mutableMapOf<KClass<*>, () -> Any>()

    fun <T: Any> register(type: KClass<T>, provider: () -> T) {
        providers[type] = provider
    }

    inline fun <reified T : Any> register(noinline provider: () -> T) {
        register(T::class, provider)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(type: KClass<T>): T {
        val p = providers[type] ?: error("No provider for ${type.qualifiedName}")
        return p.invoke() as T
    }

    inline fun <reified T : Any> get(): T = get(T::class)
}

/* ------------------------ Demo ------------------------ */

interface UserRepo { fun findAll(): List<String> }
class InMemoryUserRepo : UserRepo {
    override fun findAll(): List<String> = listOf("amy", "bob")
}

@Suppress("unused")
private fun demo() {
    val c = TinyContainer()

    c.register(UserRepo::class) { InMemoryUserRepo() }
    c.register<InMemoryUserRepo> { InMemoryUserRepo() }

    val repo1: UserRepo = c.get(UserRepo::class)
    val repo2: UserRepo = c.get()

    check(repo1.findAll() == repo2.findAll())
}
