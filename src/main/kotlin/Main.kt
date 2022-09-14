import java.io.File

fun main(args: Array<String>) {
    val markdownText = getMarkdown(args[0]) ?: return
    val markdownStruct = markdownParse(markdownText)

    createLogger(markdownStruct, File(args[0]).parent)
}

private fun getMarkdown(filePath: String): String? {
    val markdownFile = File(filePath)
    if(!markdownFile.exists() || markdownFile.extension.lowercase() != "md") {
        println("Can not find markdown file. [$filePath]")
        return null
    }

    val markdownText = markdownFile.readText()
    if(markdownText.isBlank()) return null

    return markdownText
}