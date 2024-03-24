package moe.nea.blog.md

import java.io.PrintStream

data class Document(
    val list: List<MarkdownBlock>
) : MarkdownElement {
    fun debugFormat(printStream: PrintStream) {
        list.forEach { it.debugFormat(0, printStream) }
    }
}