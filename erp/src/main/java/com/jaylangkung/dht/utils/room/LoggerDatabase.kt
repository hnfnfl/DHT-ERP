package com.jaylangkung.brainnet_staff.utils.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Logger::class], version = 1)
abstract class LoggerDatabase : RoomDatabase() {
    abstract fun loggerDao(): LoggerDao
}