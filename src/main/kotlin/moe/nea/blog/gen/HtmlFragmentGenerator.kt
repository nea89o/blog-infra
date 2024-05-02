package moe.nea.blog.gen

import moe.nea.blog.md.MarkdownElement

fun interface HtmlFragmentGenerator<T : MarkdownElement> {
	fun generateHtml(htmlGenerator: MD2HtmlGenerator, node: T): HtmlFragment
}

abstract class DefHtmlFragmentGenerator<T : MarkdownElement> : HtmlFragmentGenerator<T> {
	override fun generateHtml(htmlGenerator: MD2HtmlGenerator, node: T): HtmlFragment {
		return HtmlDsl().apply { makeHtml(htmlGenerator, node) }.intoFragment()
	}

	abstract fun HtmlDsl.makeHtml(generator: MD2HtmlGenerator, node: T)
}
