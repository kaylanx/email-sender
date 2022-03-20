package name.kayley.emailsender.model

data class EmailModel(
    val subject: String,
    val body: String,
    val csvFilePath: String,
    val attachmentFilePath: String?,
)