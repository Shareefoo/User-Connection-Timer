package com.assem.usertimer.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assem.usertimer.ui.viewmodel.UserListViewModel

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
                onDismiss = { showDialog = false },
                onSubmit = { username, macAddress ->
                    viewModel.addUser(username, macAddress, 10)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserListScreenPreview() {
    val context = LocalContext.current
    UserListScreen(viewModel = UserListViewModel(context.applicationContext as Application))
}