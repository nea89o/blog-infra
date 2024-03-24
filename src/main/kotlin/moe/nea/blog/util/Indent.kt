package moe.nea.blog.util

fun Appendable.indent(indent: Int) {
    append(" ".repeat(indent))
}