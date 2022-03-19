package name.kayley.emailsender.viewmodel

import name.kayley.emailsender.model.EmailError
import name.kayley.emailsender.model.EmailModel
import name.kayley.emailsender.provider.EmailProvider


class EmailSendViewModel(
    private val emailProvider: EmailProvider
) {
    fun sendEmail(
        email: EmailModel,
        onMessageSent: () -> Unit,
        onMessageError: (error: EmailError) -> Unit
    ) {
        emailProvider.sendEmail(
            emailModel = email,
            onMessageSent = onMessageSent,
            onMessageFailed = onMessageError
        )
    }
}