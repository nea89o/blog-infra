package moe.nea.blog.md

import java.io.PrintStream

data class Link(val label: MarkdownFormat?, val target: String) : MarkdownFormat {
    override fun debugFormat(printStream: PrintStream) {
        printStream.print("<link target=$target>")
        label?.debugFormat(printStream)
        printStream.print("</link>")
    }
}