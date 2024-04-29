package moe.nea.blog.gen

import moe.nea.blog.md.*
import kotlin.reflect.KClass

fun interface HtmlFragmentGenerator<T : MarkdownElement> {
    fun generateHtml(htmlGenerator: HtmlGenerator, node: T): HtmlFragment
}


class HtmlFragment private constructor(val text: String) {
    companion object {
        fun ofUnescaped(text: String) = HtmlFragment(
            text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
        )

        fun ofPreEscaped(text: String) = HtmlFragment(text)
        fun empty(): HtmlFragment {
            return HtmlFragment("")
        }
    }
}

annotation class HtmlDslMarker

@HtmlDslMarker
class HtmlDsl {
    val parts = mutableListOf<HtmlFragment>()

    fun append(string: String) {
        parts.add(HtmlFragment.ofUnescaped(string))
    }

    fun append(fragment: HtmlFragment) {
        parts.add(fragment)
    }

    fun appendPreEscaped(markup: String) {
        append(HtmlFragment.ofPreEscaped(markup))
    }

    operator fun String.unaryPlus() = append(this)
    operator fun HtmlFragment.unaryPlus() = append(this)

    fun element(name: String, attributes: Map<String, String>, block: HtmlDsl.() -> Unit) {
        element(name, attributes, HtmlDsl().also(block).intoFragment())
    }

    fun element(name: String, attributes: Map<String, String>, fragment: HtmlFragment) {
        appendPreEscaped(("<"))
        append(name)

        for ((key, value) in attributes) {
            appendPreEscaped((" "))
            append(key)
            appendPreEscaped(("=\""))
            append(value)
            appendPreEscaped(("\""))
        }

        appendPreEscaped((">"))

        append(fragment)

        appendPreEscaped(("</"))
        append(name)
        appendPreEscaped((">"))
    }

    fun intoFragment(): HtmlFragment {
        return HtmlFragment.ofPreEscaped(parts.joinToString("") { it.text })
    }
}


class HtmlGenerator {
    private val generators = mutableMapOf<KClass<out MarkdownElement>, HtmlFragmentGenerator<MarkdownElement>>()

    fun <T : MarkdownElement> getGeneratorFor(kClass: KClass<out T>) = generators[kClass] as HtmlFragmentGenerator<T>?

    inline fun <reified T : MarkdownElement> registerFragmentGenerator(noinline outputter: HtmlDsl.(generator: HtmlGenerator, node: T) -> Unit) {
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