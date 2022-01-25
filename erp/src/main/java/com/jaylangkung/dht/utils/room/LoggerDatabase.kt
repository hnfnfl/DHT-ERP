package com.jaylangkung.dht.utils.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Logger::class], version = 1, exportSchema = false)
abstract class LoggerDatabase : RoomDatabase() {
    abstract fun loggerDao(): LoggerDao
}