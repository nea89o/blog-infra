package moe.nea.blog.util

fun String.indentSize(): Int? {
    var i = 0
    while (i in indices) {
        if (this[i] != ' ') return i
        i++
    }
    return null
}