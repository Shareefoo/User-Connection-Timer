package com.assem.usertimer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val macAddress: String,
    val timeAdded: Long = System.currentTimeMillis(),
    var timeLeft: Long =  (10 * 1000)
)