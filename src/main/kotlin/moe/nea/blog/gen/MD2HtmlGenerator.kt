package moe.nea.blog.gen

import moe.nea.blog.md.Begin
import moe.nea.blog.md.Bold
import moe.nea.blog.md.CodeBlock
import moe.nea.blog.md.Document
import moe.nea.blog.md.FormatSequence
import moe.nea.blog.md.Header
import moe.nea.blog.md.Italics
import moe.nea.blog.md.Link
import moe.nea.blog.md.MDList
import moe.nea.blog.md.MarkdownElement
import moe.nea.blog.md.Paragraph
import moe.nea.blog.md.Whitespace
import moe.nea.blog.md.Word
import kotlin.reflect.KClass


class MD2HtmlGenerator {
	private val generators = mutableMapOf<KClass<out MarkdownElement>, HtmlFragmentGenerator<MarkdownElement>>()

	fun <T : MarkdownElement> getGeneratorFor(kClass: KClass<out T>) = generators[kClass] as HtmlFragmentGenerator<T>?

	inline fun <reified T : MarkdownElement> registerFragmentGenerator(noinline outputter: HtmlDsl.(generator: MD2HtmlGenerator, node: T) -> Unit) {
		registerGeneratorFor(T::class) { gen, node ->
			HtmlDsl()
				.apply { outputter.invoke(this, gen, node) }
				.intoFragment()
		}
	}

	inline fun <reified T : MarkdownElement> registerGeneratorFor(outputter: HtmlFragmentGenerator<T>) {
		registerGeneratorFor(T::class, outputter)
	}

	fun <T : MarkdownElement> registerGeneratorFor(kClass: KClass<T>, outputter: HtmlFragmentGenerator<T>) {
		generators[kClass] = outputter as HtmlFragmentGenerator<MarkdownElement>
	}

	fun registerDefaultGenerators() {
		registerGeneratorFor<Begin> { _, _ -> HtmlFragment.empty() }
		registerFragmentGenerator<Header> { gen, header ->
			element("h${header.level}", mapOf()) {
				+header.text
			}
		}
		registerFragmentGenerator<Bold> { gen, node ->
			element("b", mapOf(), gen.generateHtml(node.inner))
		}
		registerFragmentGenerator<Italics> { generator, node ->
			element("em", mapOf(), generator.generateHtml(node.inner))
		}
		registerFragmentGenerator<MDList> { generator, node ->
			element("ul", mapOf()) {
				for (item in node.elements) {
					element("li", mapOf()) {
						+generator.generateHtml(item)
					}
				}
			}
		}
		registerFragmentGenerator<Link> { generator, node ->
			element("a", mapOf("href" to node.target), generator.generateHtml(node.label ?: Begin()))
		}
		registerFragmentGenerator<Paragraph> { generator, node ->
			element("p", mapOf(), generator.generateHtml(node.format))
		}
		registerFragmentGenerator<CodeBlock> { generator, node ->
			element("pre", mapOf()) {
				element("code", mapOf("class" to "language-${node.language}")) {
					append(node.lines.joinToString("\n"))
				}
			}
		}
		registerFragmentGenerator<FormatSequence> { generator, node ->
			for (markdownFormat in node.list) {
				append(generator.generateHtml(markdownFormat))
			}
		}
		registerFragmentGenerator<Word> { generator, node ->
			append(node.text)
		}
		registerFragmentGenerator<Whitespace> { generator, node ->
			append(" ")
		}
		registerFragmentGenerator<Document> { generator, node ->
			for (markdownBlock in node.list) {
				append(generator.generateHtml(markdownBlock))
			}
		}
	}

	fun <T : MarkdownElement> generateHtml(node: T): HtmlFragment {
		val gen = getGeneratorFor(node::class) ?: error("Missing html generator for $node")
		return gen.generateHtml(this, node)
	}
}