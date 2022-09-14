
fun markdownParse(markdown: String): ChocoLogStruct {
    val lines = markdown.split("\r\n")
    var currentMode = LineMode.TITLE

    val struct = ChocoLogStruct(
        title = "Unknown",
        titleComment = mutableListOf(),
        logs = mutableListOf()
    )

    for ((index, line) in lines.withIndex()) {
        if (line.isBlank()) continue

        val mode = detectMode(line)
        if (mode != null) currentMode = mode

        when (mode) {
            LineMode.TITLE    -> struct.title = getValue(line)
            LineMode.LOG_NAME -> struct.logs.add(ChocoLogItemStruct(getValue(line)))
            LineMode.LOG_ARG  -> Unit
            null              -> {
                when (currentMode) {
                    LineMode.TITLE    -> struct.titleComment.add(line)
                    LineMode.LOG_NAME -> struct.logs.last().comment.add(line)
                    LineMode.LOG_ARG  -> {
                        val (arg, type) = getArg(line)
                        struct.logs.last().args[arg] = type
                    }
                }
            }
        }
    }

    return struct
}

private fun detectMode(text: String): LineMode? {
    return when {
        text.length > 2 && text.substring(0..1) == "# "   -> LineMode.TITLE
        text.length > 4 && text.substring(0..3) == "### " -> LineMode.LOG_NAME
        text.length > 2 && text.substring(0..2) == "```"  -> LineMode.LOG_ARG
        else                                              -> null
    }
}

private fun getValue(text: String): String {
    return text.split(" ")[1]
}

private fun getArg(text: String): Pair<String, String> {
    val splits = text.split(": ")
    return splits[0] to splits[1]
}

private enum class LineMode {
    TITLE, LOG_NAME, LOG_ARG
}

data class ChocoLogStruct(
    var title: String,
    var titleComment: MutableList<String>,
    var logs: MutableList<ChocoLogItemStruct>
)

data class ChocoLogItemStruct(
    var name: String,
    var args: MutableMap<String, String> = mutableMapOf(),
    var comment: MutableList<String> = mutableListOf()
)