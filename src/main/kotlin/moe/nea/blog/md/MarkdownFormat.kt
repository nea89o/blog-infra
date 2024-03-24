package moe.nea.blog.md

import java.io.PrintStream

interface MarkdownFormat : MarkdownElement {
    fun debugFormat(printStream: PrintStream)
}