package moe.nea.blog.gen

import moe.nea.blog.md.MarkdownElement
import moe.nea.blog.md.MarkdownParser
import kotlin.test.assertEquals

abstract class HtmlGenerationTest {

	open fun registerGenerators(generator: MD2HtmlGenerator) {
	}

	open fun registerParsers(parser: MarkdownParser) {
	}

	fun assertGenerator(generatedHtml: String, markdown: MarkdownElement) {
		val generator = MD2HtmlGenerator()
		generator.registerDefaultGenerators()
		registerGenerators(generator)
		assertEquals(generatedHtml, generator.generateHtml(markdown).text)
	}

	fun assertGeneratorMD(generatedHtml: String, text: String) {
		val generator = MD2HtmlGenerator()
		generator.registerDefaultGenerators()
		registerGenerators(generator)
		val parser = MarkdownParser(text)
		parser.addDefaultParsers()
		registerParsers(parser)
		assertEquals(generatedHtml, generator.generateHtml(parser.readDocument()).text)
	}
}