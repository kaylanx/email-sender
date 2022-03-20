package name.kayley.emailsender.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import name.kayley.emailsender.theme.SimpleTheme.spacer
import java.awt.FileDialog
import java.awt.Frame

@Composable
fun FileChooser(
    buttonText: String,
    defaultFilePathText: String,
    onFileChosen: ((String?) -> Unit)?
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
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(filePath)
        Spacer(modifier = Modifier.width(spacer))
        Button(onClick = {
            fileDialogOpen = true
        }) {
            Text(buttonText)
        }
        Spacer(modifier = Modifier.width(spacer))
        Button(onClick = {
            filePath = defaultFilePathText
            onFileChosen?.invoke(null)
        }) {
            Text("Clear")
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