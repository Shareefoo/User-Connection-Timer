package com.assem.usertimer.data.local.repository

import com.assem.usertimer.data.local.dao.UserDao
import com.assem.usertimer.data.local.entity.User

class UserRepository(private val userDao: UserDao) {

     fun insert(user: User) {
        userDao.insert(user)
    }

     fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

     fun update(user: User) {
        userDao.update(user)
    }

     fun delete(user: User) {
        userDao.delete(user)
    }

}