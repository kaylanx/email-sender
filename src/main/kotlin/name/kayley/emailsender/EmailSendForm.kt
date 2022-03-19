package name.kayley.emailsender

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import name.kayley.emailsender.components.FileChooser

@Composable
fun EmailSendForm() {

    var emailContent by remember { mutableStateOf("") }
    var emailSubject by remember { mutableStateOf("") }

    var attachmentToSend by remember { mutableStateOf("") }
    var csvFileChosen by remember { mutableStateOf("") }

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
            value = emailContent,
            onValueChange = {
                emailContent = it
            },
            label = { Text("Enter your email content here") },
            modifier = Modifier.height(400.dp).fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {

        }) {
            Text("Send email")
        }
    }
}