// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package name.kayley.emailsender

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    MaterialTheme {
        EmailSendForm()
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Email Sender",
        state = WindowState(width = 800.dp, height = 700.dp)
    ) {
        App()
    }
}
