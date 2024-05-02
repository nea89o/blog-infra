package moe.nea.blog.gen

import moe.nea.blog.md.MarkdownElement

fun interface HtmlFragmentGenerator<T : MarkdownElement> {
	fun generateHtml(htmlGenerator: MD2HtmlGenerator, node: T): HtmlFragment
}