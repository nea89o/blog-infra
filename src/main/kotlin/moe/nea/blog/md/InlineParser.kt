package moe.nea.blog.md

interface InlineParser {
    fun detect(lookback: MarkdownFormat, rest: String): Boolean
    val specialSyntax: Set<Char>
    fun parse(parser: MarkdownParser, text: String): Pair<MarkdownFormat, String>
}