package moe.nea.blog.gen

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