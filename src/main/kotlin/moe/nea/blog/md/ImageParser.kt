package moe.nea.blog.md

object ImageParser : InlineParser {
    override fun detect(lookback: MarkdownFormat, rest: String): Boolean {
        return rest.startsWith("![")
    }

    override val specialSyntax: Set<Char>
        get() = "!()[]".toSet()

    override fun parse(parser: MarkdownParser, text: String): Pair<MarkdownFormat, String> {
        val (label, rest) = parser.parseInlineTextUntil(
            text.substring(2),
            Begin()
        ) { _, remaining -> remaining.startsWith("]") } // TODO: better begin
        require(rest.startsWith("]("))
        val urlEndIndex = rest.indexOf(')')
        val url = rest.substring(2, urlEndIndex)
        return Pair(Image(url.trim(), parser.collapseInlineFormat(label, false)), rest.substring(urlEndIndex + 1))
    }
}