package moe.nea.blog.md.test

import kotlin.test.Test

class ParagraphTest : MarkdownTest() {

	@Test
	fun testParagraph() {
		assertDocumentFormat("""
			|a b
			|
			|c d
			""".trimMargin(), """
			|a
			|b
			|
			| 
			|c d
			|
			""".trimMargin())
	}

	@Test
	fun testCodeBlock() {
		assertDocumentFormat("""
			|a b
			|
			|c d
			|<code language=java>
			|public static final String TEST = "TEST";
			|</code>
			""".trimMargin(), """
			|a
			|b
			|
			| 
			|c d
			|```java
			|public static final String TEST = "TEST";
			|```
			|
			""".trimMargin())
	}

}