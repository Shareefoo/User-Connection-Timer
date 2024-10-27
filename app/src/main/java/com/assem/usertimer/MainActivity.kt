package com.assem.usertimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.assem.usertimer.ui.screens.UserListScreen
import com.assem.usertimer.ui.viewmodel.UserListViewModel
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