// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Frame
import java.awt.FileDialog

@Composable
@Preview
fun App() {

    var emailContent by remember { mutableStateOf("") }
    var emailSubject by remember { mutableStateOf("") }

    var attachmentToSend by remember { mutableStateOf("") }
    var csvFileChosen by remember { mutableStateOf("") }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
                    .padding(20.dp)

        ) {
            FileChooser(buttonText = "Choose csv file", defaultFilePathText = "Csv File path") {
                csvFileChosen = it
            }
            FileChooser(buttonText = "Choose email attachment file", defaultFilePathText = "Attachment File path") {
                attachmentToSend = it
            }
            OutlinedTextField(
                value = emailSubject,
                onValueChange = {
                    emailSubject = it
                },
                label = { Text("Email subject") }
            )
            OutlinedTextField(
                modifier = Modifier.height(400.dp),
                value = emailContent,
                onValueChange = {
                    emailContent = it
                },
                label = { Text("Enter your email content here") }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {

            }) {
                Text("Send email")
            }
        }
    }
}

@Composable
private fun FileChooser(
    buttonText: String,
    defaultFilePathText: String,
    onFileChosen: ((String) -> Unit)?
) {

    var filePath by remember { mutableStateOf(defaultFilePathText) }
    var fileDialogOpen by remember { mutableStateOf(false) }

    if (fileDialogOpen) {
        FileDialog(
            onCloseRequest = {
                fileDialogOpen = false
                println("Result $it")
                if (it != null) {
                    filePath = it
                    onFileChosen?.invoke(filePath)
                }
            }
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(filePath)
        Spacer(modifier = Modifier.width(20.dp))
        Button(onClick = {
            fileDialogOpen = true
        }) {
            Text(buttonText)
        }
    }
}

@Composable
private fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest("$directory$file")
                }
            }
        }
    },
    dispose = FileDialog::dispose
)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Email Sender"
    ) {
        App()
    }
}
