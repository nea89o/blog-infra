package moe.nea.blog.md

import moe.nea.blog.util.indentSize
import java.util.*


class MarkdownParser(source: String) {
    private val lines = source.lines()
    private var lineIndex = 0
    private var blockIndents = 0
    private val indentStack = Stack<Int>()

    private val blockParsers = mutableListOf<BlockParser>()
    private val inlineParsers = mutableListOf<InlineParser>()

    fun findParserFor(line: String): BlockParser? {
        return blockParsers.filter { it.detect(line) }
            .maxByOrNull { it.prio }
    }

    fun readChildBlock(): MarkdownBlock? {
        val peek = peekLine() ?: return null
        val blockParser = findParserFor(peek) ?: ParagraphParser
        return blockParser.parse(this)
    }

    fun pushIndent(newIndent: Int) {
        require(newIndent > blockIndents)
        indentStack.push(blockIndents)
        blockIndents = newIndent
    }

    fun popIndent() {
        blockIndents = indentStack.pop()
    }

    fun consumeLine(): String? {
        val line = peekLine()
        if (line != null)
            lineIndex++
        return line
    }

    fun peekLine(): String? {
        if (lineIndex !in lines.indices) return null
        val line = lines[lineIndex]
        val indent = line.indentSize()
        if (indent != null && indent < blockIndents)
            return null
        return line.substring(blockIndents)
    }

    fun parseInlineTextOnce(lookback: MarkdownFormat, text: String): Pair<MarkdownFormat, String> {
        val parser = inlineParsers.find { it.detect(lookback, text) }
        if (parser != null)
            return parser.parse(this, text)
        require(!text.isEmpty()) // TODO handle empty string
        if (text[0] == ' ')
            return Pair(Whitespace(), text.substring(1))
        val nextSpecial = text.indexOfFirst { it in inlineParsers.flatMap { it.specialSyntax } || it == ' ' }
        return Pair(Word(text.substring(0, nextSpecial)), text.substring(nextSpecial))
    }

    fun parseInlineText(text: String): MarkdownFormat {
        val seq = mutableListOf<MarkdownFormat>()
        var remaining = text
        var lastToken: MarkdownFormat = Begin()
        while (remaining.isNotEmpty()) {
            val (tok, next) = parseInlineTextOnce(lastToken, remaining)
            seq.add(tok)
            lastToken = tok
            remaining = next
        }
        return collapseInlineFormat(seq)
    }

    fun collapseInlineFormat(sequence: List<MarkdownFormat>): MarkdownFormat {
        return FormatSequence(sequence)
    }

    fun readDocument(): Document {
        val list = mutableListOf<MarkdownBlock>()
        while (true) {
            val block = readChildBlock() ?: break
            list.add(block)
        }
        return Document(list)
    }

    fun addDefaultParsers() {
        blockParsers.add(CodeBlockParser)
        blockParsers.add(HeaderParser)
        inlineParsers.add(ItalicsParser)
    }
}

