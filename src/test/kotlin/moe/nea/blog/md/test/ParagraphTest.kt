package moe.nea.blog.md.test

import moe.nea.blog.md.MarkdownParser
import moe.nea.blog.md.ext.NoteBlockParser
import kotlin.test.Test

class ParagraphTest : MarkdownTest() {
	override fun registerExtraParser(parser: MarkdownParser) {
		parser.addParser(NoteBlockParser)
	}

	@Test
	fun testNonImmediateParagraph() {
		assertDocumentFormat(
			"""
			|<note type=note>
			|  <code language=assembly>
			|  xor eax, eax
			|  ret
			|  </code>
			|</note>
			|<list>
			|  <element>
			|    <p>
			|    Test
			|    </p>
			|    <code language=assembly>
			|    inc eax
			|    ret
			|    </code>
			|  </element>
			|</list>
			""".trimMargin(),
			"""
			|!!! note
			|
			|   ```assembly
			|   xor eax, eax
			|   ret
			|   ```
			|
			| - Test
			|  
			|   ```assembly
			|   inc eax
			|   ret
			|   ```
			""".trimMargin(),
		)
	}

	@Test
	fun testParagraph() {
		assertDocumentFormat("""
			|<p>
			|a b
			|</p>
			|<p>
			|c d
			|</p>
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
			|<p>
			|a b
			|</p>
			|<p>
			|c d
			|</p>
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