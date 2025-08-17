package lab.reified

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

inline fun <reified T> Gson.fromJsonReified(json: String): T {
    val type: Type = object : TypeToken<T>() {}.type
    return this.fromJson(json, type)
}

fun <T> Gson.fromJsonWithType(json: String, type: Type): T =
    this.fromJson(json, type)

@Suppress("unused")
private fun demo() {
    val gson = Gson()

    val ints: List<Int> = gson.fromJsonReified("[1,2,3]")
    val map: Map<String, Any> = gson.fromJsonReified("""{"a":1,"b":"x"}""")

    val listType: Type = object : TypeToken<List<Int>>() {}.type
    val ints2: List<Int> = gson.fromJsonWithType("[1,2,3]", listType)

    val mapType: Type = object : TypeToken<Map<String, Any>>() {}.type
    val map2: Map<String, Any> = gson.fromJsonWithType("""{"a":1,"b":"x"}""", mapType)

    check(ints == ints2)
    check(map.keys == map2.keys)
}