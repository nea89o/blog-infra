package moe.nea.blog.md

import java.io.PrintStream

class Whitespace : MarkdownFormat {
    override fun debugFormat(printStream: PrintStream) {
        printStream.print(" ")
    }
}