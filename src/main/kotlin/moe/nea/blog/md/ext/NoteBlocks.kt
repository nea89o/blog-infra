package moe.nea.blog.md.ext

import moe.nea.blog.gen.DefHtmlFragmentGenerator
import moe.nea.blog.gen.HtmlDsl
import moe.nea.blog.gen.MD2HtmlGenerator
import moe.nea.blog.md.BlockParser
import moe.nea.blog.md.MarkdownBlock
import moe.nea.blog.md.MarkdownParser
import moe.nea.blog.util.indent
import moe.nea.blog.util.indentSize
import java.io.PrintStream

data class NoteBlock(val noteType: String, val child: MarkdownBlock) : MarkdownBlock {
	override fun debugFormat(indent: Int, printStream: PrintStream) {
		printStream.indent(indent)
		printStream.println("<note type=$noteType>")

		child.debugFormat(indent + 2, printStream)

		printStream.indent(indent)
		printStream.println("</note>")
	}
}

object NoteBlockGenerator : DefHtmlFragmentGenerator<NoteBlock>() {
	override fun HtmlDsl.makeHtml(generator: MD2HtmlGenerator, node: NoteBlock) {
		element("div", mapOf("class" to "note-${node.noteType}"), generator.generateHtml(node.child))
	}
}

object NoteBlockParser : BlockParser {
	override fun detect(line: String): Boolean {
		return line.startsWith("!!!")
	}

	override fun parse(parser: MarkdownParser): MarkdownBlock {
		val line = parser.consumeLine()!!
		val noteType = line.substring(3).trim()
		while ((parser.peekLine() ?: error("Unterminated !!! note")).isBlank()) {
			parser.consumeLine()
		}
		val indent = parser.peekLine()!!.indentSize()!!
		require(indent > 0) { "!!! note not indented" }
		parser.pushIndent(indent)
		val list = mutableListOf<MarkdownBlock>()
		while (true) {
			val block = parser.readChildBlock() ?: break
			list.add(block)
		}
		parser.popIndent()
		return NoteBlock(noteType, parser.mergeBlocks(list))
	}

	override val prio: Int
		get() = 10

}
