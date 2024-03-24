package moe.nea.blog.md

object BlockQuoteParser : BlockParser {
    override fun detect(line: String): Boolean {
        return line.startsWith("> ")
    }

    override fun parse(parser: MarkdownParser): MarkdownBlock {
        parser.pushPreProcessor(
            LinePreProcessor.whileIgnoringFirst(
                parser.getIndent()
            ) { _, line ->
                if (!line.startsWith("> ") && line != ">") {
                    null
                } else {
                    "  " + line.substring(2)
                }
            }
        )
        parser.pushIndent(2)
        val blocks = mutableListOf<MarkdownBlock>()
        while (true) {
            val child = parser.readChildBlock() ?: break
            blocks.add(child)
        }
        parser.popPreProcessor()
        parser.popIndent()
        return BlockQuote(parser.mergeBlocks(blocks))
    }

    override val prio: Int
        get() = 10
}