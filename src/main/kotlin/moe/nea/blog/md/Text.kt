package moe.nea.blog.md

import java.io.PrintStream

data class Text(val text: String) : MarkdownFormat {
    override fun debugFormat(printStream: PrintStream) {
        printStream.print(printStream)
    }
}