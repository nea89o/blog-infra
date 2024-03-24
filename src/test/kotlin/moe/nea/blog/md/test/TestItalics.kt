package moe.nea.blog.md.test

import moe.nea.blog.md.MarkdownFormat
import moe.nea.blog.md.MarkdownParser
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals

class TestItalics {

    fun parseInline(string: String): MarkdownFormat {
        val parser = MarkdownParser(string)
        parser.addDefaultParsers()
        return parser.parseInlineText(string)
    }

    fun assertInlineFormat(expected: String, source: String) {
        val format = parseInline(source)
        val baos = ByteArrayOutputStream()
        format.debugFormat(PrintStream(baos))
        assertEquals(expected, baos.toByteArray().decodeToString())
    }

    @Test
    fun testDoubleStartItalicsInBold() {
        assertInlineFormat("<b><i>both</i> just bold</b>", "***both* just bold**")
    }

    @Test
    fun testDoubleStartBoldInItalics() {
        assertInlineFormat("<i><b>both</b> just italics</i>", "***both** just italics*")
    }

    @Test
    fun testDoubleStartRestartingItalics() {
        assertInlineFormat(
            "<i><b>both</b> just italics <b>both again</b></i>",
            "***both** just italics **both again***"
        )
    }

    @Test
    fun testStandaloneBold() {
        assertInlineFormat("<b>bold</b>", "**bold**")
    }

    @Test
    fun testStandaloneItalics() {
        assertInlineFormat("<i>italic</i>", "*italic*")
    }

    @Test
    fun testInline() {
        assertInlineFormat("left <i>italics</i> middle <b>bold</b> right", "left *italics* middle **bold** right")
    }

    @Test
    fun testFreestandingStar() {
        assertInlineFormat("left * right", "left * right")
    }

    @Test
    fun testFreestandingStarInItalics() {
        assertInlineFormat("<i>left * right</i>", "*left * right*")
    }

    @Test
    fun testNested() {
        assertInlineFormat("<i>a <b>bold</b> b</i>", "*a **bold** b*")
        assertInlineFormat("<i>a <i>double italics</i></i>", "*a *double italics**")
    }

}