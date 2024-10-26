package com.assem.usertimer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assem.usertimer.util.MacAddressVisualTransformation

@Composable
fun AddUserDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var macAddress by remember { mutableStateOf("") }

    val macAddressRegex =
        Regex("([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})|([0-9a-fA-F]{4}\\.[0-9a-fA-F]{4}\\.[0-9a-fA-F]{4})")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New User", style = MaterialTheme.typography.bodyLarge) },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = macAddress,
                    onValueChange = { newText ->
                        if (newText.length <= 12) {
                            macAddress = newText
                        }
                    },
                    label = { Text("MAC Address") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = MacAddressVisualTransformation(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            ElevatedButton(onClick = { onSubmit(username, macAddress); onDismiss() }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AddUserDialogPreview() {
    AddUserDialog(onDismiss = {}, onSubmit = { _, _ -> })
}