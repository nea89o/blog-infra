package moe.nea.blog.md

import moe.nea.blog.util.indent
import java.io.PrintStream

data class CodeBlock(val language: String, val lines: List<String>) : MarkdownBlock {
    override fun debugFormat(indent: Int, printStream: PrintStream) {
        printStream.indent(indent)
        printStream.println("<code lang=$language>")
        lines.forEach {
            printStream.indent(indent)
            printStream.println(it)
        }
        printStream.indent(indent)
        printStream.println("</code>")
    }
}

