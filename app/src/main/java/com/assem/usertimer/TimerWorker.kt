package com.assem.usertimer

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class TimerWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val workerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val userDao = AppDatabase.getDatabase(context).userDao()

    override suspend fun doWork(): Result {

        val userId = inputData.getInt("userId", 0)
        if (userId == 0) return Result.failure()

        return workerScope.async {
            try {
                var currentTimeLeft = userDao.getTimeLeft(userId).first()

                while (currentTimeLeft > 0 && !isStopped) {
                    delay(1000)
                    currentTimeLeft = userDao.getTimeLeft(userId).first()
                    currentTimeLeft -= 1
                    userDao.updateTimeLeft(userId, currentTimeLeft)
                }

                if (!isStopped) {
                    sendNotification(applicationContext, userId)
                }

                Result.success()

            } catch (e: Exception) {
                Result.failure()
            }
        }.await()
    }


    private suspend fun sendNotification(context: Context, userId: Int) {
        Log.d("DDD", "sendNotification")
        val user = userDao.getAllUsers().first().find { it.id == userId }
        if (user != null) {
            val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Time's up for ${user.username}")
//                .setContentText("Please take a break.")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your notification icon
                .build()
            notificationManager?.notify(user.id, notification)
        }
    }

    companion object {
        const val CHANNEL_ID = "countdown_channel"
    }

}