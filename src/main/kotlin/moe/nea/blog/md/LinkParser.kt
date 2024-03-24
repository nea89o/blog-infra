package moe.nea.blog.md

object LinkParser : InlineParser {
    override fun detect(lookback: MarkdownFormat, rest: String): Boolean {
        return rest.startsWith("[")
    }

    override val specialSyntax: Set<Char>
        get() = "()[]".toSet()

    override fun parse(parser: MarkdownParser, text: String): Pair<MarkdownFormat, String> {
        val (label, rest) = parser.parseInlineTextUntil(
            text.substring(1),
            Begin()
        ) { lookback, remaining -> remaining.startsWith("]") } // TODO: better begin
        require(rest.startsWith("](")) { TODO("unlabeled links") }
        val urlEndIndex = rest.indexOf(')')
        val url = rest.substring(2, urlEndIndex)
        return Pair(Link(parser.collapseInlineFormat(label, false), url.trim()), rest.substring(urlEndIndex + 1))
    }
}