package moe.nea.blog.md.test

import kotlin.test.Test

class CodeBlockTest : MarkdownTest() {
    @Test
    fun testJavaCodeBlock() {
        assertDocumentFormat(
            """
            <code language=java>
            public class ObjectControllerFactoryManagerProvider<T extends ObjectControllerFactoryManager<T>> {
                public void doTheThing() {}

            }
            </code>
            """.trimIndent(),
            """
            ```java 
            public class ObjectControllerFactoryManagerProvider<T extends ObjectControllerFactoryManager<T>> {
                public void doTheThing() {}

            }
            ```
            """.trimIndent()
        )
    }
    @Test
    fun testAutoStripCommonIndent() {
        assertDocumentFormat(
            """
            <code language=java>
            public class ObjectControllerFactoryManagerProvider<T extends ObjectControllerFactoryManager<T>> {
                public void doTheThing() {}
            }
            </code>
            """.trimIndent(),
            """
            ```java 
                public class ObjectControllerFactoryManagerProvider<T extends ObjectControllerFactoryManager<T>> {
                    public void doTheThing() {}
                }
            ```
            """.trimIndent()
        )
    }

    @Test
    fun testNonStrippedBlock() {
        assertDocumentFormat(
            """
            <code language=java>
                public class ObjectControllerFactoryManagerProvider<T extends ObjectControllerFactoryManager<T>> {
                    public void doTheThing() {}
                }
            </code>
            """.trimIndent(),
            """
            ```java notrim
                public class ObjectControllerFactoryManagerProvider<T extends ObjectControllerFactoryManager<T>> {
                    public void doTheThing() {}
                }
            ```
            """.trimIndent()
        )
    }
}