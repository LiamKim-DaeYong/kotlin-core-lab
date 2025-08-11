package lab.inline

fun <T, R> normalOp(value: T, block: (T) -> R): R = block(value)

inline fun <T, R> inlineOp(value: T, block: (T) -> R): R = block(value)

fun callSiteNormal(): Int =
    normalOp(10) { it * 2 }

fun callSiteInline(): Int =
    inlineOp(10) { it * 2 }

val doubleF: (Int) -> Int = { x -> x * 2 }

fun callSiteNormal_var(): Int =
    normalOp(10, doubleF)

fun callSiteInline_var(): Int =
    inlineOp(10, doubleF)

fun callSiteInline_capture(): Int {
    val k = 3
    return inlineOp(10) { it * k }
}