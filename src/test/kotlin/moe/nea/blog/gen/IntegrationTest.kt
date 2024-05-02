package moe.nea.blog.gen

import moe.nea.blog.md.MarkdownParser
import moe.nea.blog.md.ext.NoteBlockGenerator
import moe.nea.blog.md.ext.NoteBlockParser
import moe.nea.blog.test.util.GetResource
import kotlin.test.Test

class IntegrationTest : HtmlGenerationTest() {
	override fun registerGenerators(generator: MD2HtmlGenerator) {
		generator.registerGeneratorFor(NoteBlockGenerator)
	}

	override fun registerParsers(parser: MarkdownParser) {
		parser.addParser(NoteBlockParser)
	}

	@Test
	fun testHttps() {
		assertGeneratorMD(GetResource.asString("/https.html"), GetResource.asString("/https.md"))
	}

}