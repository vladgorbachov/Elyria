package com.elyria.app.res

import org.junit.Assert.assertEquals
import org.junit.Test
import org.w3c.dom.Document
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class StringResourceKeysTest {

    @Test
    fun allLocalesHaveSameStringKeys() {
        val resDir = File("src/main/res")
        val defaultKeys = keysIn(File(resDir, "values/strings.xml"))
        val locales = listOf("values-ru", "values-uk", "values-ro")
        locales.forEach { folder ->
            val localeKeys = keysIn(File(resDir, "$folder/strings.xml"))
            assertEquals(
                "Key mismatch in $folder",
                defaultKeys,
                localeKeys,
            )
        }
    }

    private fun keysIn(file: File): Set<String> {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc: Document = builder.parse(file)
        val nodes = doc.getElementsByTagName("string")
        return buildSet {
            for (i in 0 until nodes.length) {
                val node = nodes.item(i)
                add(node.attributes.getNamedItem("name").nodeValue)
            }
        }
    }
}
