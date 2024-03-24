package moe.nea.blog.md.test

import moe.nea.blog.md.Document
import moe.nea.blog.md.MarkdownFormat
import moe.nea.blog.md.MarkdownParser
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

abstract class MarkdownTest {

    fun parseInline(string: String): MarkdownFormat {
        val parser = MarkdownParser(string)
        parser.addDefaultParsers()
        return parser.parseInlineText(string)
    }
    fun parseDoc(string: String): Document {
        val parser = MarkdownParser(string)
        parser.addDefaultParsers()
        return parser.readDocument()
    }

    fun assertDocumentFormat(expected: String, source: String) {
        val format = parseDoc(source)
        val baos = ByteArrayOutputStream()
        format.debugFormat(PrintStream(baos))
        assertEquals(expected.trim(), baos.toByteArray().decodeToString().trim())
    }

    fun assertInlineFormat(expected: String, source: String) {
        val format = parseInline(source)
        val baos = ByteArrayOutputStream()
        format.debugFormat(PrintStream(baos))
        assertEquals(expected, baos.toByteArray().decodeToString())
    }
}