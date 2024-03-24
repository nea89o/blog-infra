package moe.nea.blog.md

import java.io.PrintStream

data class Italics(val inner: MarkdownFormat) : MarkdownFormat {
    override fun debugFormat(printStream: PrintStream) {
        printStream.print("<i>")
        inner.debugFormat(printStream)
        printStream.print("</i>")
    }
}