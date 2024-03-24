package moe.nea.blog.md.test

import kotlin.test.Test

class BlockQuoteTest : MarkdownTest() {
    @Test
    fun testNestedBlockQuote() {
        assertDocumentFormat(
            """
            <quote>
              hi
              <quote>
                hello
              </quote>
            </quote>
            """.trimIndent(),
            """
            > hi    
            > > hello
            """.trimIndent()
        )
    }

    @Test
    fun testCodeInBlock() {
        assertDocumentFormat(
            """
            <quote>
              hi
              <code language=kotlin>
              fun helloWorld(): Unit =
                  "Hello, World!".let(::println)
              </code>
            </quote>
            """.trimIndent(),
            """
            > hi    
            > ```kotlin
            > fun helloWorld(): Unit =
            >     "Hello, World!".let(::println)
            > ```
            """.trimIndent()
        )
    }
}