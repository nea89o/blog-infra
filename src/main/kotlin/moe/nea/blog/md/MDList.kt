package moe.nea.blog.md

import moe.nea.blog.util.indent
import java.io.PrintStream

data class MDList(
    val elements: List<MarkdownBlock>
) : MarkdownBlock {
    override fun debugFormat(indent: Int, printStream: PrintStream) {
        printStream.indent(indent)
        printStream.println("<list>")

        elements.forEach {
            printStream.indent(indent + 2)
            printStream.println("<element>")
            it.debugFormat(indent + 4, printStream)
            printStream.indent(indent + 2)
            printStream.println("</element>")
        }

        printStream.indent(indent)
        printStream.println("</list>")
    }
}