package com.assem.usertimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assem.usertimer.ui.theme.UserTimerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserTimerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: UserListViewModel by viewModels()
                    UserListScreen(viewModel)
                }
            }
        }
    }
}


@Composable
fun UserListItem(user: User, viewModel: UserListViewModel) {

    val clipboardManager = LocalClipboardManager.current

    val timeLeft = user.timeLeft

    val hours = timeLeft / 3600
    val minutes = (timeLeft % 3600) / 60
    val seconds = timeLeft % 60

    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    val backgroundColor = if (timeLeft == 0) {
        Color.LightGray
    } else {
        colorResource(id = R.color.greenLight)
    }

    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .clickable {
                    clipboardManager.setText(AnnotatedString(user.macAddress.formatMacAddress()))
                }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.username,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 15.sp)
            )
            Text(
                text = user.macAddress.formatMacAddress(),
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 15.sp)
            )
            Text(
                text = formattedTime, modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 15.sp)
            )
            IconButton(
                modifier = Modifier.weight(0.5f),
                onClick = { viewModel.addTimeToUser(user.id, 10 * 60) }) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add 10 minutes")
            }
            IconButton(
                modifier = Modifier.weight(0.5f),
                onClick = { viewModel.deleteUser(user) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }

}

@Composable
fun UserListScreen(viewModel: UserListViewModel) {

    val users by viewModel.users.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp)) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Username",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "MAC Address",
                        modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Time",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "+10",
                        modifier = Modifier.weight(0.5f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "X",
                        modifier = Modifier.weight(0.5f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
            items(users) { user ->
                UserListItem(user, viewModel)
            }
        }

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .width(200.dp)
                .height(60.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Add New User", style = TextStyle(fontSize = 17.sp))
        }

        if (showDialog) {
            AddUserDialog(
                onDismissRequest = { showDialog = false },
                onSubmit = { username, macAddress ->
                    viewModel.addUser(username, macAddress, 10)
                }
            )
        }
    }
}

@Composable
fun AddUserDialog(
    onDismissRequest: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var macAddress by remember { mutableStateOf("") }

    val macAddressRegex =
        Regex("([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})|([0-9a-fA-F]{4}\\.[0-9a-fA-F]{4}\\.[0-9a-fA-F]{4})")

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add New User", style = MaterialTheme.typography.bodySmall) },
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
            ElevatedButton(onClick = { onSubmit(username, macAddress); onDismissRequest() }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun UserListScreenPreview() {
    UserTimerTheme {
//        val userListViewModel =
//            UserListViewModel(LocalContext.current.applicationContext as Application)
//        userListViewModel.addUser("User 1", "11:22:33:44")
//        userListViewModel.addUser("User 2", "11:22:33:44")
//        userListViewModel.addUser("User 3", "11:22:33:44")
//        UserListScreen(userListViewModel)
    }
}