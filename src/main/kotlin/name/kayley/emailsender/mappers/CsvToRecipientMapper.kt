package name.kayley.emailsender.mappers

import java.io.File

object CsvToRecipientMapper {

    private const val BOM = "\uFEFF"

    fun processLineByLine(csv: File, processor: (Map<String, String>) -> Unit) {
        val header = csv.useLines { it.firstOrNull()?.replace(BOM, "")?.split(",") }
            ?: throw Exception("This file does not contain a valid header")

        csv.useLines { linesSequence ->
            linesSequence
                .drop(1)
                .map { it.split(",") }
                .map { header.zip(it).toMap() }
                .forEach(processor)
        }
    }
}