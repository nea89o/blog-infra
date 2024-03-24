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
    private val preprocessors = Stack<LinePreProcessor>()
    private var peekedLine: String? = null

    fun findParserFor(line: String): BlockParser? {
        return blockParsers.filter { it.detect(line) }
            .maxByOrNull { it.prio }
    }

    fun readChildBlock(): MarkdownBlock? {
        val peek = peekLine() ?: return null
        val blockParser = findParserFor(peek) ?: ParagraphParser
        return blockParser.parse(this)
    }

    fun pushPreProcessor(preProcessor: LinePreProcessor) {
        peekedLine = null
        preprocessors.push(preProcessor)
    }

    fun popPreProcessor() {
        peekedLine = null
        preprocessors.pop()
    }

    fun preProcessLine(string: String): String? {
        var acc = string
        for (processor in preprocessors) {
            acc = processor.preprocess(lineIndex, acc) ?: return null
        }
        return acc
    }

    fun pushIndent(newIndent: Int) {
        indentStack.push(blockIndents)
        blockIndents += newIndent
        peekedLine = null
    }

    fun popIndent() {
        blockIndents = indentStack.pop()
        peekedLine = null
    }

    fun unpeekLine() {
        peekedLine = null
    }

    fun consumeLine(): String? {
        val line = peekLine()
        if (line != null) {
            peekedLine = null
            lineIndex++
        }
        return line
    }

    fun peekLine(): String? {
        if (lineIndex !in lines.indices) return null
        val line = peekedLine ?: preProcessLine(lines[lineIndex]) ?: return null
        peekedLine = line
        val indent = line.indentSize()
        if (indent != null && indent < blockIndents) {
            peekedLine = null
            return null
        }
        return line.drop(blockIndents)
    }

    fun parseInlineTextUntil(
        text: String,
        initialLookback: MarkdownFormat,
        breakout: (lookback: MarkdownFormat, remaining: String) -> Boolean
    ): Pair<List<MarkdownFormat>, String> {
        val seq = mutableListOf<MarkdownFormat>()
        var remaining = text
        var lastToken: MarkdownFormat = initialLookback
        while (remaining.isNotEmpty()) {
            val (tok, next) = parseInlineTextOnce(lastToken, remaining)
            seq.add(tok)
            lastToken = tok
            remaining = next
            if (breakout(tok, next))
                break
        }
        return seq to remaining
    }

    fun parseInlineTextOnce(lookback: MarkdownFormat, text: String): Pair<MarkdownFormat, String> {
        require(text.isNotEmpty()) // TODO handle empty string
        val parser = inlineParsers.find { it.detect(lookback, text) }
        if (parser != null) {
            return parser.parse(this, text)
        }
        if (text[0] == ' ') {
            return Pair(Whitespace(), text.substring(1))
        }
        val nextSpecial = text.indexOfFirst { it in inlineParsers.flatMap { it.specialSyntax } || it == ' ' }
        if (nextSpecial == 0) {
            return Pair(Word(text.substring(0, 1)), text.substring(1))
        }
        if (nextSpecial == -1) {
            return Pair(Word(text), "")
        }
        return Pair(Word(text.substring(0, nextSpecial)), text.substring(nextSpecial))
    }

    fun parseInlineText(text: String): MarkdownFormat {
        val (tokens, rest) = parseInlineTextUntil(text, Begin()) { _, _ -> false }
        require(rest.isEmpty())
        return collapseInlineFormat(tokens, true)
    }

    private fun expandMarkdownFormats(sequence: List<MarkdownFormat>): List<MarkdownFormat> {
        val elongated = mutableListOf<MarkdownFormat>()
        for (markdownFormat in sequence) {
            if (markdownFormat is FormatSequence) {
                elongated.addAll(expandMarkdownFormats(markdownFormat.list))
            } else {
                elongated.add(markdownFormat)
            }
        }
        return elongated
    }

    private fun collapseMarkdownFormats(
        sequence: List<MarkdownFormat>,
        trimWhitespace: Boolean
    ): MutableList<MarkdownFormat> {
        val shortened = mutableListOf<MarkdownFormat>()
        var last: MarkdownFormat = if (trimWhitespace) Whitespace() else Begin()
        for (format in sequence) {
            if (format is Whitespace && last is Whitespace) {
                continue
            }
            last = format
            shortened.add(format)
        }
        return shortened
    }

    fun collapseInlineFormat(sequence: List<MarkdownFormat>, trimWhitespace: Boolean): MarkdownFormat {
        val formats = collapseMarkdownFormats(expandMarkdownFormats(sequence), trimWhitespace)
        return formats.singleOrNull() ?: FormatSequence(formats)
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
        blockParsers.add(ListParser)
        inlineParsers.add(ItalicsParser)
        inlineParsers.add(LinkParser)
    }

    fun getLineIndex(): Int {
        return lineIndex
    }

    fun mergeBlocks(elements: List<MarkdownBlock>): MarkdownBlock {
        return elements.singleOrNull() ?: BlockList(elements)
    }

    fun getIndent(): Int {
        return blockIndents
    }
}

