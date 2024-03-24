package moe.nea.blog.md

import java.io.PrintStream

data class BlockList(val elements: List<MarkdownBlock>) : MarkdownBlock {
    override fun debugFormat(indent: Int, printStream: PrintStream) {
        elements.forEach {
            it.debugFormat(indent, printStream)
        }
    }

}
