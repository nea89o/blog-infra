package moe.nea.blog.md

object ItalicsParser : InlineParser {
    override fun detect(lookback: MarkdownFormat, rest: String): Boolean {
        return lookback is Whitespace && "\\*+[^ ].*".toRegex().matches(rest)
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

        val isBold = firstStarCount >= 2
        val isItalics = (firstStarCount % 2) == 1

        val firstSequence = mutableListOf<MarkdownFormat>()
        var remainingText = text.substring(firstStarCount)
        var lastToken: MarkdownFormat = Begin() // TODO: dedicated begin token
        while (remainingText.isNotEmpty()) {
            val (element, next) = parser.parseInlineTextOnce(lastToken, remainingText)
            remainingText = next
            lastToken = element
            firstSequence.add(element)
            if (element !is Whitespace && next.startsWith("*")) break
        }

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
            if (isBold)
                firstElement = Bold(firstElement)
            if (isItalics)
                firstElement = Italics(firstElement)
            return Pair(firstElement, remainingText)
        }

        val secondSequence = mutableListOf<MarkdownFormat>()
        lastToken = Begin()
        while (remainingText.isNotEmpty()) {
            val (element, next) = parser.parseInlineTextOnce(lastToken, remainingText)
            remainingText = next
            lastToken = element
            secondSequence.add(element)
            if (element !is Whitespace && next.startsWith("*")) break
        }

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
        var secondElement = parser.collapseInlineFormat(secondSequence, false)
        var combined: MarkdownFormat = FormatSequence(firstElement, secondElement)
        if (thirdStarCount == 1)
            combined = Italics(combined)
        if (thirdStarCount == 2)
            combined = Bold(combined)
        return Pair(combined, remainingText)
    }

}