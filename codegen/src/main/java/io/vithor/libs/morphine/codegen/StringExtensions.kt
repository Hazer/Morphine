package io.vithor.libs.morphine.codegen

inline fun String.takeWhileIndexed(predicate: (Int, Char) -> Boolean): String {
    for (index in 0 until length)
        if (!predicate(index, get(index))) {
            return substring(0, index)
        }
    return this
}