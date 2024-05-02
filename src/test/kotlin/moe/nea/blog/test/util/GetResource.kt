package moe.nea.blog.test.util

object GetResource {
	fun asString(path: String): String {
		return GetResource::class.java.getResourceAsStream(path)!!.bufferedReader().readText()
	}
}