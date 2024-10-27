package com.assem.usertimer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.assem.usertimer.R
import com.assem.usertimer.data.local.entity.User
import com.assem.usertimer.ui.viewmodel.UserListViewModel
import com.assem.usertimer.util.formatMacAddress
import kotlinx.coroutines.delay

@Composable
fun UserListItem(user: User, viewModel: UserListViewModel = viewModel()) {

    val clipboardManager = LocalClipboardManager.current

    var remainingTime by remember { mutableStateOf(viewModel.getRemainingTime(user)) }

    val backgroundColor = if (remainingTime == 0L) {
        Color.LightGray
    } else {
        colorResource(id = R.color.greenLight)
    }

    LaunchedEffect(key1 = user.id) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime = viewModel.getRemainingTime(user)
        }
    }

    val displayTime = formatTime(remainingTime)

    Card(modifier = Modifier.padding(8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                    style = TextStyle(fontSize = 17.sp)
                )
                Text(
                    text = user.macAddress.formatMacAddress(),
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 17.sp)
                )
                Text(
                    text = displayTime,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 17.sp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.addTime(user) }) {
                    Icon(
                        painterResource(id = R.drawable.add_circle_24),
                        contentDescription = "Add 10 minutes"
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.removeTime(user) }) {
                    Icon(
                        painterResource(id = R.drawable.remove_circle_24),
                        contentDescription = "Remove 10 minutes"
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.deleteUser(user) }) {
                    Icon(
                        painterResource(id = R.drawable.delete_24),
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }

}

fun formatTime(timeInMillis: Long): String {
    val totalSeconds = (timeInMillis / 1000).toInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

@Preview(showBackground = true)
@Composable
fun UserListItemPreview() {
    UserListItem(User(0, "Test", "12:F4:CD:78:90:11"))
}