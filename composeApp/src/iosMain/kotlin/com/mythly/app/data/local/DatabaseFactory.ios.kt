package com.mythly.app.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<MythlyDatabase> {
    val dbFilePath = NSHomeDirectory() + "/mythly.db"
    return Room.databaseBuilder<MythlyDatabase>(
        name = dbFilePath,
    )
}
