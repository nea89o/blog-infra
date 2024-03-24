package moe.nea.blog.md

object ParagraphParser : BlockParser {
    override fun detect(line: String): Boolean {
        return true
    }

    override fun parse(parser: MarkdownParser): MarkdownBlock {
        var text = ""
        var emptyLineCount = 0
        while (true) {
            val line = parser.peekLine() ?: break
            if (parser.findParserFor(line) != null) break
            val trimmedLine = parser.consumeLine()!!.trim()
            if (trimmedLine == "") {
                emptyLineCount++
            } else {
                emptyLineCount = 0
            }
            if (emptyLineCount == 1) {
                break
            }
            text += " $trimmedLine"
        }
        return Paragraph(parser.parseInlineText(text))
    }

    override val prio: Int
        get() = -10

}