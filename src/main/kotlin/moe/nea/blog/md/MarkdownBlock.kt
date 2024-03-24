package moe.nea.blog.md

import java.io.PrintStream

interface MarkdownBlock : MarkdownElement {
    fun debugFormat(indent: Int, printStream: PrintStream)
}