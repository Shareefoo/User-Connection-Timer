package com.assem.usertimer.ui.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.assem.usertimer.data.local.database.AppDatabase
import com.assem.usertimer.data.local.entity.User
import com.assem.usertimer.service.TimerService
import com.assem.usertimer.worker.TimerWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserListViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val database = AppDatabase.getDatabase(application)

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    init {
        viewModelScope.launch {
            userDao.getAllUsers().collect { _users.value = it }
        }
    }

    fun addUser(username: String, macAddress: String, initialTime: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(username = username, macAddress = macAddress, timeLeft = initialTime)
            val userId = userDao.insert(user)
            startTimerWorker(user.copy(id = userId.toInt()), initialTime)
            startTimerService(userId.toInt(), initialTime)
        }
    }

    fun addTimeToUser(userId: Int, addedTime: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            database.withTransaction {
                val currentTimeLeft = userDao.getTimeLeft(userId).first()
                val newTimeLeft = currentTimeLeft + addedTime
                userDao.updateTimeLeft(userId, newTimeLeft)
            }

            _users.value = _users.value.map { user ->
                if (user.id == userId) {
                    user.copy(timeLeft = userDao.getTimeLeft(userId).first())
                } else {
                    user
                }
            }

            val user = _users.value.find { it.id == userId }
            if (user != null) {
                startTimerWorker(user, user.timeLeft)
            }
        }
    }

    fun getTimeLeft(userId: Int): StateFlow<Int> {
        val timeLeftFlow = MutableStateFlow(0)

        viewModelScope.launch(Dispatchers.IO) {
            userDao.getTimeLeft(userId).collect { timeLeft ->
                timeLeftFlow.value = timeLeft
            }
        }
        return timeLeftFlow.asStateFlow()
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.delete(user)
        }
    }

    fun startTimerWorker(user: User, initialTime: Int) {
        val data = workDataOf("userId" to user.id, "initialTime" to initialTime)
        val workRequest = OneTimeWorkRequestBuilder<TimerWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(getApplication()).enqueueUniqueWork(
            "countdown_worker_${user.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun startTimerService(userId: Int, initialTime: Int) {
        val serviceIntent = Intent(getApplication(), TimerService::class.java)
        serviceIntent.putExtra("userId", userId)
        serviceIntent.putExtra("initialTime", initialTime)
        ContextCompat.startForegroundService(getApplication(), serviceIntent)
    }

}