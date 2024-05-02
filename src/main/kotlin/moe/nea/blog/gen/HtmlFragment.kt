package moe.nea.blog.gen

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