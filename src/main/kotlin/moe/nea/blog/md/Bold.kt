package moe.nea.blog.md

import java.io.PrintStream

data class Bold(val inner: MarkdownFormat) : MarkdownFormat {
    override fun debugFormat(printStream: PrintStream) {
        printStream.print("<b>")
        inner.debugFormat(printStream)
        printStream.print("</b>")
    }
}