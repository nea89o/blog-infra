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
        val processor = object : LinePreProcessor {
            override fun preprocess(lineIndex: Int, line: String): String? {
                if (line.substring(indentDepth).startsWith(prefix)) {
                    if (bypassLineIndex == lineIndex) {
                        return line.substring(0, indentDepth) +
                                " ".repeat(indentSize + 2) +
                                line.substring(indentDepth + indentSize + 2)
                    } else {
                        return null
                    }
                }
                return line
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