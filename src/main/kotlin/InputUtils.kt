import java.io.File

internal object InputUtils {

    private fun getFromResources(file : String) : File {
        val uri = InputUtils.javaClass.classLoader.getResource (file)?.toURI()
                ?: throw IllegalArgumentException("File was not found")
        return File(uri)
    }

    fun getLines(file: String) : List<String> {
        return getFromResources(file).readLines().toCollection(arrayListOf())
    }

    fun getString(file : String) : String {
        return getFromResources(file).readText()
    }

    fun getStrings(file : String, separator : String = "\n") : List<String> {
        return getString(file).split(separator)
    }

}