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

}