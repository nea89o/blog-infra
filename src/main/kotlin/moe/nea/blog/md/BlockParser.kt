package moe.nea.blog.md

interface BlockParser {
    fun detect(line: String): Boolean
    fun parse(parser: MarkdownParser): MarkdownBlock
    val prio: Int
}