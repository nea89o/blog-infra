package moe.nea.blog.md

object CodeBlockParser : BlockParser {
    override fun detect(line: String): Boolean {
        return line.startsWith("```")
    }

    override fun parse(parser: MarkdownParser): MarkdownBlock {
        val language = parser.consumeLine()!!.removePrefix("```")
        val lines = mutableListOf<String>()
        while (true) {
            val line = parser.consumeLine() ?: error("Unfinished code block")
            if (line == "```")
                break
            lines.add(line)
        }
        return CodeBlock(language, lines)
    }

    override val prio: Int
        get() = 10

}