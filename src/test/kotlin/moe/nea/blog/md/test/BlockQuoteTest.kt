package moe.nea.blog.md.test

import kotlin.test.Test

class BlockQuoteTest : MarkdownTest() {
    @Test
    fun testNestedBlockQuote() {
        assertDocumentFormat(
            """
            <quote>
              <p>
              hi
              </p>
              <quote>
                <p>
                hello
                </p>
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
              <p>
              hi
              </p>
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