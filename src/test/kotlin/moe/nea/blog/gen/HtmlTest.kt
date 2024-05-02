package moe.nea.blog.gen

import moe.nea.blog.md.Bold
import moe.nea.blog.md.FormatSequence
import moe.nea.blog.md.Paragraph
import moe.nea.blog.md.Whitespace
import moe.nea.blog.md.Word
import kotlin.test.Test

class HtmlTest : HtmlGenerationTest() {

	@Test
	fun testBiggerFile() {
		assertGeneratorMD(
			"""
			|<h1>Hello World</h1><p></p><p><b><em>lol</em> hehe</b></p><pre><code class="language-java">public class ObjectControllerFactoryManagerProvider&lt;T extends ObjectControllerFactoryManager&lt;T&gt;&gt; {
			|   public ObjectControllerFactoryManagerProvider() {}
			|}</code></pre>
			""".trimMargin(),
			"""
			|# Hello World
			|
			|***lol*  hehe**
			|
			|```java
			|public class ObjectControllerFactoryManagerProvider<T extends ObjectControllerFactoryManager<T>> {
			|   public ObjectControllerFactoryManagerProvider() {}
			|}
			|```
			""".trimMargin()
		)
	}

	@Test
	fun testBold() {
		assertGenerator("<b>Hii</b>", Bold(Word("Hii")))
	}

	@Test
	fun testParagraphs() {
		assertGenerator(
			"<p>Test <b>Whatever</b></p>",
			Paragraph(FormatSequence(Word("Test"), Whitespace(), Bold(Word("Whatever"))))
		)
	}

}