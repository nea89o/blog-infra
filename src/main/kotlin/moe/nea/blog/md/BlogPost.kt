package moe.nea.blog.md

import java.io.File


fun main() {
    val parser = MarkdownParser(File("data/test.md").readText())
    parser.addDefaultParsers()
    val doc = parser.readDocument()
    doc.debugFormat(System.out)
}
