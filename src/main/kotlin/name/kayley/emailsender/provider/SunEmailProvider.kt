package name.kayley.emailsender.provider

import name.kayley.emailsender.model.EmailError
import name.kayley.emailsender.model.EmailModel
import name.kayley.emailsender.properties
import java.io.File
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class SunEmailProvider : EmailProvider {

    override fun sendEmail(
        emailModel: EmailModel,
        onMessageSent: () -> Unit,
        onMessageFailed: (errors: List<EmailError>) -> Unit
    ) {

        print("emailModel = $emailModel")
        val passwordAuthentication = object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(
                    properties.getProperty("email.username"),
                    properties.getProperty("email.password")
                )
            }
        }

        val lineSeparator = System.getProperty("line.separator")

        val session: Session = Session.getInstance(properties, passwordAuthentication)

        val emailErrors = mutableListOf<EmailError>()
        emailModel.recipients.forEach { emailRecipient ->
            try {
                val message = MimeMessage(session)
                with(message) {
                    setFrom(
                        InternetAddress(
                            properties.getProperty("email.username"),
                            properties.getProperty("email.fromname")
                        )
                    )
                    setRecipients(
                        Message.RecipientType.TO,
                        arrayOf(InternetAddress(emailRecipient.emailAddress, emailRecipient.name))
                    )
                    subject = emailModel.subject
                }

                var msg = emailModel.body
                    .replace("#NAME", emailRecipient.name)
                    .replace(lineSeparator, "<br/>")

                msg += "<br/><br/><br/>"

                val mimeBodyPart = MimeBodyPart()
                mimeBodyPart.setContent(msg, "text/html; charset=utf-8")

                val multipart = MimeMultipart()
                multipart.addBodyPart(mimeBodyPart)

                print("attempting to attach ${emailModel.attachmentFilePaths}")
                emailModel.attachmentFilePaths?.forEach { filePath ->
                    val attachmentPart = MimeBodyPart()
                    attachmentPart.attachFile(File(filePath))
                    multipart.addBodyPart(attachmentPart)
                }

                message.setContent(multipart)
                Transport.send(message)
            } catch (ex: Exception) {
                ex.printStackTrace()
                emailErrors.add(EmailError(recipient = emailRecipient, description = ex.message ?: "UNKNOWN"))
            }
        }

        if (emailErrors.isNotEmpty()) {
            onMessageFailed(emailErrors)
        } else {
            onMessageSent()
        }
    }
}