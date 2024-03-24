package moe.nea.blog.md

fun interface LinePreProcessor {
    fun preprocess(lineIndex: Int, line: String): String?

    companion object {
        fun whileIgnoringFirst(indent: Int, wrapped: LinePreProcessor): LinePreProcessor {
            return LinePreProcessor { lineIndex, line ->
                line.take(indent) + wrapped.preprocess(lineIndex, line.drop(indent))
            }
        }
    }
}
