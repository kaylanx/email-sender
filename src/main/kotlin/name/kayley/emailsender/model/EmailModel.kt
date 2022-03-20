package name.kayley.emailsender.model

data class EmailModel(
    val subject: String,
    val body: String,
    val recipients: List<EmailRecipient>,
    val attachmentFilePaths: List<String>?,
)