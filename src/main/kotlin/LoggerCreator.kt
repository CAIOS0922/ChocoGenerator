import java.io.File

fun createLogger(struct: ChocoLogStruct, parent: String) {
    val outputFile = File(parent, "${struct.title}.kt")
    val builder = StringBuilder()

    for (comment in struct.titleComment)  builder.appendLine("// $comment")
    builder.appendLine("sealed class ${struct.title}: ChocoItem() {")

    for (log in struct.logs) {
        builder.appendLine()

        for (comment in log.comment) builder.appendLine("// $comment")

        if(log.args.isEmpty()) {
            builder.appendLine("class ${log.name}: ${struct.title}() {")
            builder.appendLine("override val title: String = \"${getLogTitle(log.name).camelToSnakeCase()}\"")
            builder.appendLine("override val properties: Bundle = Bundle()")
            builder.appendLine("}")
        } else {
            builder.appendLine("class ${log.name}(")

            for ((arg, type) in log.args) builder.appendLine("$arg: $type,")

            builder.appendLine("): ${struct.title}() {")

            builder.appendLine("override val title: String = \"${getLogTitle(log.name).camelToSnakeCase()}\"")
            builder.appendLine("override val properties: Bundle = Bundle().apply {")

            for ((arg, type) in log.args) builder.appendLine("put$type(\"${arg.camelToSnakeCase()}\", $arg)")

            builder.appendLine("}")
            builder.appendLine("}")
        }
    }

    builder.appendLine()
    builder.appendLine("companion object {")

    for (log in struct.logs) {
        if (log.args.isEmpty()) {
            builder.appendLine("fun ${getLogFuncName(log.name)}() = ${log.name}()")
        } else {
            val argsInClass = log.args.map { it.key }.joinToString(separator = ",")
            val argsInFunc = log.args.map { "${it.key}: ${it.value}" }.joinToString(separator = ",")
            builder.appendLine("fun ${getLogFuncName(log.name)}($argsInFunc) = ${log.name}($argsInClass)")
        }
    }

    builder.appendLine("}")
    builder.appendLine("}")

    outputFile.writeText(builder.toString())
}


private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private val snakeRegex = "_[a-zA-Z]".toRegex()

private fun String.camelToSnakeCase(): String {
    return camelRegex.replace(this) { "_${it.value}" }.lowercase()
}

private fun getLogTitle(name: String): String {
    if(name.length > 3 && name.substring(0..1).lowercase() == "on") return name.removeRange(0..1)
    return name
}

private fun getLogFuncName(name: String): String {
    val firstChar = name.first().lowercaseChar()
    val elseString = name.substring(1)

    return firstChar + elseString
}