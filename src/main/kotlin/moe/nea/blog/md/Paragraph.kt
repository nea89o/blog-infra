package moe.nea.blog.md

import moe.nea.blog.util.indent
import java.io.PrintStream

data class Paragraph(val format: MarkdownFormat) : MarkdownBlock {
    override fun debugFormat(indent: Int, printStream: PrintStream) {
        printStream.indent(indent)
        format.debugFormat(printStream)
        printStream.println()
    }
}