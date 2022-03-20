package name.kayley.emailsender.provider

import name.kayley.emailsender.model.EmailError
import name.kayley.emailsender.model.EmailModel

interface EmailProvider {
    fun sendEmail(
        emailModel: EmailModel,
        onMessageSent: () -> Unit,
        onMessageFailed: (errors: List<EmailError>) -> Unit
    )
}