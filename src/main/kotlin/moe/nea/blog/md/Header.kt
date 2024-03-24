package moe.nea.blog.md

import moe.nea.blog.util.indent
import java.io.PrintStream

data class Header(val level: Int, val text: String) : MarkdownBlock {
    override fun debugFormat(indent: Int, printStream: PrintStream) {
        printStream.indent(indent)
        printStream.println("header level=$level: $text")
    }
}