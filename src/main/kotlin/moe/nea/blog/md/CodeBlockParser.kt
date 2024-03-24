package moe.nea.blog.md

import moe.nea.blog.util.indentSize

object CodeBlockParser : BlockParser {
    override fun detect(line: String): Boolean {
        return line.startsWith("```")
    }

    override fun parse(parser: MarkdownParser): MarkdownBlock {
        val tags = parser.consumeLine()!!.removePrefix("```").trim().split(" +".toRegex())
        val language = tags.firstOrNull() ?: ""
        val lines = mutableListOf<String>()
        while (true) {
            val line = parser.consumeLine() ?: error("Unfinished code block")
            if (line == "```")
                break
            lines.add(line)
        }
        val commonIndent =
            if (tags.contains("notrim")) 0
            else lines.minOfOrNull { it.indentSize() ?: Int.MAX_VALUE } ?: 0
        return CodeBlock(language, lines.map { it.drop(commonIndent) })
    }

    override val prio: Int
        get() = 10

}