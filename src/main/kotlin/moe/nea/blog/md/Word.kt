package moe.nea.blog.md

import java.io.PrintStream

data class Word(val text: String) : MarkdownFormat {
    override fun debugFormat(printStream: PrintStream) {
        printStream.print(text)
    }
}