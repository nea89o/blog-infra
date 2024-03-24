package moe.nea.blog.md

import moe.nea.blog.util.indent
import java.io.PrintStream

class BlockQuote(val inner: MarkdownBlock) : MarkdownBlock {
    override fun debugFormat(indent: Int, printStream: PrintStream) {
        printStream.indent(indent)
        printStream.println("<quote>")
        inner.debugFormat(indent + 2, printStream)
        printStream.indent(indent)
        printStream.println("</quote>")
    }
}