package name.kayley.emailsender

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import name.kayley.emailsender.components.FileChooser
import name.kayley.emailsender.model.EmailError
import name.kayley.emailsender.model.EmailModel
import name.kayley.emailsender.provider.SunEmailProvider
import name.kayley.emailsender.theme.SimpleTheme.dialogWidth
import name.kayley.emailsender.theme.SimpleTheme.formPadding
import name.kayley.emailsender.theme.SimpleTheme.spacerHeight
import name.kayley.emailsender.theme.SimpleTheme.textAreaHeight
import name.kayley.emailsender.viewmodel.EmailSendViewModel

@Composable
fun EmailSendForm(
    viewModel: EmailSendViewModel = EmailSendViewModel(SunEmailProvider())
) {
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    val requestSent = remember { mutableStateOf(false) }
    val emailSent = remember { mutableStateOf(false) }
    val emailSendError = remember { mutableStateOf<EmailError?>(null) }

    var emailBody by remember { mutableStateOf("") }
    var emailSubject by remember { mutableStateOf("") }

    var attachmentToSend by remember { mutableStateOf("") }
    var csvFileChosen by remember { mutableStateOf("") }

    EmailsSentDialog(emailSent = emailSent)
    ErrorDialog(emailSendError = emailSendError)

    Column(
        modifier = Modifier.fillMaxWidth().padding(formPadding)
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
            modifier = Modifier.height(textAreaHeight)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(spacerHeight))
        Button(onClick = {
            requestSent.value = true
            val emailModel = EmailModel(subject = emailSubject, body = emailBody)
            coroutineScope.launch {
                viewModel.sendEmail(
                    email = emailModel,
                    onMessageSent = {
                        emailSent.value = true
                        requestSent.value = false
                    },
                    onMessageError = {
                        emailSendError.value = it
                        requestSent.value = false
                    }
                )
            }
        }) {
            if (requestSent.value) {
                CircularProgressIndicator(
                    color = Color.White
                )
            } else {
                Text("Send email")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ErrorDialog(emailSendError: MutableState<EmailError?>) {
    if (emailSendError.value != null) {
        val error = emailSendError.value
        AlertDialog(
            modifier = Modifier.width(dialogWidth),
            onDismissRequest = {},
            title = {
                Text(
                    text = "Error",
                )
            },
            text = {
                Text(
                    text = error?.description ?: "Unknown Error",
                )
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EmailsSentDialog(emailSent: MutableState<Boolean>) {
    if (emailSent.value) {
        AlertDialog(
            modifier = Modifier.width(dialogWidth),
            onDismissRequest = {},
            title = {
                Text(
                    text = "Done"
                )
            },
            text = {
                Text(
                    text = "Email(s) sent",
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        emailSent.value = false
                    }) {
                    Text("OK")
                }
            }
        )
    }
}