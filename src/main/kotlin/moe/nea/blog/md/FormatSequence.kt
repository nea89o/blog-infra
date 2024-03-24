package moe.nea.blog.md

import java.io.PrintStream

data class FormatSequence(val list: List<MarkdownFormat>) : MarkdownFormat {
    constructor(vararg list: MarkdownFormat) : this(list.toList())

    override fun debugFormat(printStream: PrintStream) {
        list.forEach { it.debugFormat(printStream) }
    }
}