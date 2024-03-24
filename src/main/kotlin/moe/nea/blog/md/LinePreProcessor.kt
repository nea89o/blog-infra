package moe.nea.blog.md

interface LinePreProcessor {
    fun preprocess(lineIndex: Int, line: String): String?
}
