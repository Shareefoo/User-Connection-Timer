package com.assem.usertimer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.assem.usertimer.data.local.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Long

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("UPDATE users SET timeLeft = :timeLeft WHERE id = :userId")
    fun updateTimeLeft(userId: Int, timeLeft: Int)

    @Query("SELECT timeLeft FROM users WHERE id = :userId")
    fun getTimeLeft(userId: Int): Flow<Int>

    @Delete
    fun delete(user: User)

}