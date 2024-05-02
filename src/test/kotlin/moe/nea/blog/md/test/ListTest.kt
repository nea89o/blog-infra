package moe.nea.blog.md.test

import kotlin.test.Test

class ListTest : MarkdownTest() {
    @Test
    fun testBasicList() {
        assertDocumentFormat(
            """
            <p>
            Some Text:
            </p>
            <list>
              <element>
                <p>
                First
                </p>
              </element>
              <element>
                <p>
                Second
                </p>
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
            <p>
            Some Text:
            </p>
            <list>
              <element>
                <p>
                First More First
                </p>
              </element>
              <element>
                <p>
                Second
                </p>
                <p>
                More Second
                </p>
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
            <p>
            Some Text:
            </p>
            <list>
              <element>
                <p>
                First
                </p>
                <list>
                  <element>
                    <p>
                    Another List
                    </p>
                  </element>
                </list>
              </element>
              <element>
                <p>
                Second
                </p>
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