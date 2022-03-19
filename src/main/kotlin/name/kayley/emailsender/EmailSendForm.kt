package name.kayley.emailsender

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import name.kayley.emailsender.components.FileChooser
import name.kayley.emailsender.model.EmailError
import name.kayley.emailsender.model.EmailModel
import name.kayley.emailsender.provider.SunEmailProvider
import name.kayley.emailsender.viewmodel.EmailSendViewModel

@Composable
fun EmailSendForm(
    viewModel: EmailSendViewModel = EmailSendViewModel(SunEmailProvider())
) {

    val emailSent = remember { mutableStateOf(false)  }
    val emailSendError = remember { mutableStateOf<EmailError?>(null) }

    var emailBody by remember { mutableStateOf("") }
    var emailSubject by remember { mutableStateOf("") }

    var attachmentToSend by remember { mutableStateOf("") }
    var csvFileChosen by remember { mutableStateOf("") }

    ErrorDialog(emailSendError)

    Column(
        modifier = Modifier.fillMaxWidth().padding(20.dp)
    ) {
        FileChooser(buttonText = "Choose csv file", defaultFilePathText = "Csv File path") {
            csvFileChosen = it
        }
        FileChooser(buttonText = "Choose email attachment file", defaultFilePathText = "Attachment File path") {
            attachmentToSend = it
        }
        OutlinedTextField(value = emailSubject, onValueChange = {
            emailSubject = it
        }, label = { Text("Email subject") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = emailBody,
            onValueChange = {
                emailBody = it
            },
            label = { Text("Enter your email content here") },
            modifier = Modifier.height(400.dp).fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            val emailModel = EmailModel(subject = emailSubject, body = emailBody)
            viewModel.sendEmail(
                email = emailModel,
                onMessageSent = {
                    emailSent.value = true
                },
                onMessageError = {
                    emailSendError.value = it
                }
            )
        }) {
            Text("Send email")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ErrorDialog(emailSendError: MutableState<EmailError?>) {
    if (emailSendError.value != null) {
        val error = emailSendError.value
        AlertDialog(
            onDismissRequest = {
                emailSendError.value = null
            },
            title = {
                Text(text = "Error")
            },
            text = {
                Text(error?.description ?: "Unknown Error")
            },
            confirmButton = {
                Button(
                    onClick = {
                        emailSendError.value = null
                    }) {
                    Text("OK")
                }
            }
        )
    }
}