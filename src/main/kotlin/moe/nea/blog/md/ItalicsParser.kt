package moe.nea.blog.md

object ItalicsParser : InlineParser {
    override fun detect(lookback: MarkdownFormat, rest: String): Boolean {
        return (lookback is Whitespace || lookback is Begin) && "\\*+[^* ].*".toRegex().matches(rest)
    }

    override val specialSyntax: Set<Char>
        get() = setOf('*')

    override fun parse(parser: MarkdownParser, text: String): Pair<MarkdownFormat, String> {
        var firstStarCount = 0
        while (firstStarCount in text.indices) {
            if (text[firstStarCount] != '*') break
            firstStarCount++
        }

        if (firstStarCount < 1 || firstStarCount > 3) error("Invalid italics/bold sequence")

        var (firstSequence, remainingText) = parser.parseInlineTextUntil(
            text.substring(firstStarCount),
            Begin()
        ) { lookback, remaining -> lookback !is Whitespace && remaining.startsWith("*") } // TODO: free standing *

        var secondStarCount = 0
        while (secondStarCount in remainingText.indices) {
            if (remainingText[secondStarCount] != '*') break
            secondStarCount++
        }

        if (secondStarCount > firstStarCount)
            secondStarCount = firstStarCount
        if (secondStarCount < 1) error("Invalid italics/bold sequence")

        remainingText = remainingText.substring(secondStarCount)
        var firstElement = parser.collapseInlineFormat(firstSequence, false)
        if (secondStarCount == 2)
            firstElement = Bold(firstElement)
        if (secondStarCount == 1)
            firstElement = Italics(firstElement)
        if (secondStarCount == firstStarCount) {
            return Pair(firstElement, remainingText)
        }

        // TODO: better begin
        val (secondSequence, _remainingText) = parser.parseInlineTextUntil(remainingText, Begin()) { lookback, remaining ->
            lookback !is Whitespace && remaining.startsWith("*")
        }
        remainingText = _remainingText

        var thirdStarCount = 0
        while (thirdStarCount in remainingText.indices) {
            if (remainingText[thirdStarCount] != '*') break
            thirdStarCount++
        }

        if (thirdStarCount > firstStarCount - secondStarCount)
            thirdStarCount = firstStarCount

        remainingText = remainingText.substring(thirdStarCount)

        if (thirdStarCount != firstStarCount - secondStarCount) {
            error("Invalid italics/bold sequence")
        }
        val secondElement = parser.collapseInlineFormat(secondSequence, false)
        var combined: MarkdownFormat = FormatSequence(firstElement, secondElement)
        if (thirdStarCount == 1)
            combined = Italics(combined)
        if (thirdStarCount == 2)
            combined = Bold(combined)
        return Pair(combined, remainingText)
    }
}