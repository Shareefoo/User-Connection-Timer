package com.assem.usertimer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val macAddress: String,
    val timeLeft: Int = 10 //3 * 60 * 60
)