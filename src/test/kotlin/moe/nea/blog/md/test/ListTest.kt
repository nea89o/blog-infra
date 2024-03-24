package moe.nea.blog.md.test

import kotlin.test.Test

class ListTest : MarkdownTest() {
    @Test
    fun testBasicList() {
        assertDocumentFormat(
            """
            Some Text:
            <list>
              <element>
                First
              </element>
              <element>
                Second
              </element>
            </list>
            """.trimIndent(),
            """
            Some Text:
             - First
             - Second
            """.trimIndent()
        )
    }
    @Test
    fun testBlockContinuation() {
        assertDocumentFormat(
            """
            Some Text:
            <list>
              <element>
                First More First
              </element>
              <element>
                Second
                More Second
              </element>
            </list>
            """.trimIndent(),
            """
            Some Text:
             - First
               More First
             - Second
             
               More Second
            """.trimIndent()
        )
    }
    @Test
    fun testNestedLists() {
        assertDocumentFormat(
            """
            Some Text:
            <list>
              <element>
                First
                <list>
                  <element>
                    Another List
                  </element>
                </list>
              </element>
              <element>
                Second
              </element>
            </list>
            """.trimIndent(),
            """
            Some Text:
             - First
                - Another List
             - Second
            """.trimIndent()
        )
    }
    @Test
    fun testCodeBlockInList() {
        assertDocumentFormat(
            """
            <list>
              <element>
                <code language=java>
                public class Test {
                
                   public static void main(String...args) {}
                }
                </code>
              </element>
            </list>
            """.trimIndent(),
            """
             - ```java
                 public class Test {
                 
                    public static void main(String...args) {}
                 }
               ```
            """.trimIndent()
        )
    }
}