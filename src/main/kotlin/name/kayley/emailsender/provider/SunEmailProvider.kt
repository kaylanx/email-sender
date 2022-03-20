package name.kayley.emailsender.provider

import name.kayley.emailsender.model.EmailError
import name.kayley.emailsender.model.EmailModel
import java.io.File
import java.util.*
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
                return PasswordAuthentication("username", "password")
            }
        }

        val session: Session = Session.getInstance(getProperties(), passwordAuthentication)

        val emailErrors = mutableListOf<EmailError>()
        emailModel.recipients.forEach { emailRecipient ->
            try {
                val message = MimeMessage(session)
                message.setFrom(InternetAddress("andykayley@gmail.com", "Andy Kayley"))
                message.setRecipients(
                    Message.RecipientType.TO,
                    arrayOf(InternetAddress(emailRecipient.emailAddress, emailRecipient.name))
                )
                message.subject = emailModel.subject

                val lineSeparator = System.getProperty("line.separator")
                var msg = emailModel.body
                    .replace("#NAME", emailRecipient.name)
                    .replace(lineSeparator, "<br/>")

                msg += "<br/><br/><br/>"

                val mimeBodyPart = MimeBodyPart()
                mimeBodyPart.setContent(msg, "text/html; charset=utf-8")
                val multipart: Multipart = MimeMultipart()
                multipart.addBodyPart(mimeBodyPart)

                if (emailModel.attachmentFilePath != null) {
                    print("attempting to attach ${emailModel.attachmentFilePath}")
                    val attachmentPart = MimeBodyPart()
                    attachmentPart.attachFile(File(emailModel.attachmentFilePath))
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

    private fun getProperties(): Properties {
        val properties = Properties()
        properties["mail.smtp.auth"] = true
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "587"
        properties["mail.smtp.ssl.trust"] = "smtp.gmail.com"
        return properties
    }
}