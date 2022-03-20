package name.kayley.emailsender.provider

import name.kayley.emailsender.model.EmailError
import name.kayley.emailsender.model.EmailModel
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
        onMessageError: (error: EmailError) -> Unit
    ) {

        val passwordAuthentication = object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("username", "password")
            }
        }

        val session: Session = Session.getInstance(getProperties(), passwordAuthentication)

        val message: Message = MimeMessage(session)
        message.setFrom(InternetAddress("andykayley@gmail.com"))
        message.setRecipients(
            Message.RecipientType.TO, InternetAddress.parse("andykayley@gmail.com")
        )
        message.subject = emailModel.subject

        val msg = emailModel.body

        val mimeBodyPart = MimeBodyPart()
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8")

        val multipart: Multipart = MimeMultipart()
        multipart.addBodyPart(mimeBodyPart)

        message.setContent(multipart)

        try {
            Transport.send(message)
            onMessageSent()
        } catch (me: MessagingException) {
            me.printStackTrace()
            onMessageError(EmailError(code = 999, description = me.message ?: "UNKNOWN"))
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