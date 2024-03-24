package moe.nea.blog.md

object HeaderParser : BlockParser {
    override fun detect(line: String): Boolean {
        return line.matches("#+ .*".toRegex())
    }

    override fun parse(parser: MarkdownParser): MarkdownBlock {
        val line = parser.consumeLine()!!
        val level = line.indexOf(" ")
        val text = line.substring(level).trim()
        return Header(level, text)
    }

    override val prio: Int
        get() = 10
}