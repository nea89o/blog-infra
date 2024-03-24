package moe.nea.blog.md.test

import kotlin.test.Test

class LinkTest : MarkdownTest() {
    @Test
    fun testPaddedLink() {
        assertInlineFormat("<link target=https://nea.moe>meee</link>", "[meee]( https://nea.moe )")
    }

    @Test
    fun testFormattingInLink() {
        assertInlineFormat("<link target=https://nea.moe><i>meee</i></link>", "[*meee*]( https://nea.moe )")
    }

    @Test
    fun testBasicImage() {
        assertInlineFormat(
            "<image source=https://nea.moe/image.jxl>alt text <i>here</i></image>",
            "![alt text *here*]( https://nea.moe/image.jxl )"
        )
    }

    @Test
    fun testImageLink() {
        assertInlineFormat(
            "<link target=https://nea.moe><image source=https://nea.moe/image.jxl></image></link>",
            "[![]( https://nea.moe/image.jxl )](https://nea.moe)"
        )
    }

}