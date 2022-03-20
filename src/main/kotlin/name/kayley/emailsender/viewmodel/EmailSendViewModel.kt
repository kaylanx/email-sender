package name.kayley.emailsender.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import name.kayley.emailsender.mappers.CsvToRecipientMapper
import name.kayley.emailsender.model.EmailError
import name.kayley.emailsender.model.EmailModel
import name.kayley.emailsender.model.EmailRecipient
import name.kayley.emailsender.provider.EmailProvider
import java.io.File


class EmailSendViewModel(
    private val emailProvider: EmailProvider,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val coroutineScope = CoroutineScope(dispatcher)

    fun sendEmail(
        email: EmailModel,
        onMessageSent: () -> Unit,
        onMessageError: (errors: List<EmailError>) -> Unit
    ) {
        coroutineScope.launch {
            emailProvider.sendEmail(
                emailModel = email,
                onMessageSent = onMessageSent,
                onMessageFailed = onMessageError
            )
        }
    }

    fun getRecipients(csvFilePath: String): List<EmailRecipient> {
        val file = File(csvFilePath)
        val emailRecipients = mutableListOf<EmailRecipient>()
        CsvToRecipientMapper.processLineByLine(file) { row ->
            val name = row["name"]
            val email = row["email"]
            if (name != null && email != null) {
                emailRecipients.add(EmailRecipient(name = name, emailAddress = email))
            } else {
                print("Error processing row $row")
            }
        }
        return emailRecipients
    }
}