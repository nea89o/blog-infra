package moe.nea.blog.md

import moe.nea.blog.util.indentSize

object ListParser : BlockParser {
    override fun detect(line: String): Boolean {
        return line.matches(" *[*+-] .*".toRegex())
    }

    override fun parse(parser: MarkdownParser): MarkdownBlock {
        val line = parser.peekLine()!!
        val indentSize = line.indentSize()!!
        val prefix = line.substring(0, indentSize + 2)
        var bypassLineIndex = parser.getLineIndex()
        val indentDepth = parser.getIndent()
        val processor = LinePreProcessor.whileIgnoringFirst(indentDepth) { lineIndex, line ->
            if (line.startsWith(prefix)) {
                if (bypassLineIndex == lineIndex) {
                    " ".repeat(indentSize + 2) + line.substring(indentSize + 2)
                } else {
                    null
                }
            } else {
                line
            }
        }
        parser.pushPreProcessor(processor)
        parser.pushIndent(indentSize + 2)

        val listItems = mutableListOf<MarkdownBlock>()

        while (true) {
            bypassLineIndex = parser.getLineIndex()
            val elements = mutableListOf<MarkdownBlock>()
            while (true) {
                val child = parser.readChildBlock() ?: break
                elements.add(child)
            }
            parser.popPreProcessor()
            parser.popIndent()
            val realLine = parser.peekLine()
            parser.pushPreProcessor(processor)
            parser.pushIndent(indentSize + 2)
            listItems.add(parser.mergeBlocks(elements))
            if (realLine == null || !realLine.startsWith(prefix)) {
                break
            }
        }

        parser.popIndent()
        parser.popPreProcessor()
        return MDList(listItems)
    }

    override val prio: Int
        get() = 10
}