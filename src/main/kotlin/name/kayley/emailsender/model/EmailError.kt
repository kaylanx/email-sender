package name.kayley.emailsender.model

data class EmailError(
    val recipient: EmailRecipient,
    val description: String
)
