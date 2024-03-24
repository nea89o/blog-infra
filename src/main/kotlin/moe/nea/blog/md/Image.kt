package moe.nea.blog.md

import java.io.PrintStream

data class Image(val source: String, val alt: MarkdownFormat?) : MarkdownFormat {
    override fun debugFormat(printStream: PrintStream) {
        printStream.print("<image source=$source>")
        alt?.debugFormat(printStream)
        printStream.print("</image>")
    }
}